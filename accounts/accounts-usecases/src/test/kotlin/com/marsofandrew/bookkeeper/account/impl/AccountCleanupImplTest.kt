package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.fixtures.account
import com.marsofandrew.bookkeeper.base.date
import com.marsofandrew.bookkeeper.properties.id.asId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Clock
import java.time.Instant
import java.time.Period
import java.time.ZoneId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AccountCleanupImplTest {

    private val accountStorage = mockk<AccountStorage>(relaxUnitFun = true)
    private val clock = Clock.fixed(Instant.now(), ZoneId.of("Z"))
    private val periodForFiltering = Period.of(0, 0, 1)

    private lateinit var accountCleanupImpl: AccountCleanupImpl

    @BeforeEach
    fun setup() {
        accountCleanupImpl = AccountCleanupImpl(
            accountStorage,
            clock
        ) { days > periodForFiltering.days }
    }

    @Test
    fun `clean when there are accounts for removal then deletes that accounts`() {
        val batchSize = 2
        val accounts = listOf(
            setOf(
                account("1".asId(), 5.asId()) {
                    status = Account.Status.FOR_REMOVAL
                    closedAt = clock.date().minusDays(5)
                },
                account("2".asId(), 4.asId()) {
                    status = Account.Status.FOR_REMOVAL
                    closedAt = clock.date().minusDays(3)
                }
            ),
            setOf(
                account("3".asId(), 6.asId()) {
                    status = Account.Status.FOR_REMOVAL
                    closedAt = clock.date().minusDays(4)
                },
                account("4".asId(), 4.asId()) {
                    status = Account.Status.FOR_REMOVAL
                    closedAt = clock.date().minusDays(3)
                }
            ),
            setOf(),
        )

        every { accountStorage.findAccountsForRemoval(batchSize) } returnsMany accounts

        accountCleanupImpl.clean(batchSize)

        verify(exactly = accounts.size) { accountStorage.findAccountsForRemoval(batchSize) }
        verify(exactly = 3) { accountStorage.delete(any()) }
    }

    @Test
    fun `clean when accounts that set for removal don not match filter criteria then do nothing`() {
        val batchSize = 2
        val accounts = setOf(
            account("1".asId(), 5.asId()) {
                status = Account.Status.FOR_REMOVAL
                closedAt = clock.date()
            },
            account("2".asId(), 4.asId()) {
                status = Account.Status.FOR_REMOVAL
                closedAt = clock.date()
            }
        )

        every { accountStorage.findAccountsForRemoval(batchSize) } returns accounts

        accountCleanupImpl.clean(batchSize)

        verify(exactly = 1) { accountStorage.findAccountsForRemoval(batchSize) }
        verify(exactly = 1) { accountStorage.delete(emptySet()) }
    }
}