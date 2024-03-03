package com.marsofandrew.bookkeeper.accounts.impl

import com.marsofandrew.bookkeeper.accounts.Account
import com.marsofandrew.bookkeeper.accounts.access.AccountStorage
import com.marsofandrew.bookkeeper.accounts.fixtures.account
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.base.date
import com.marsofandrew.bookkeeper.properties.id.asId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AccountDeletionImplTest {

    private val accountStorage = mockk<AccountStorage>(relaxUnitFun = true)
    private val clock = Clock.fixed(Instant.now(), ZoneId.of("Z"))

    private lateinit var accountDeletionImpl: AccountDeletionImpl

    @BeforeEach
    fun setup() {
        accountDeletionImpl = AccountDeletionImpl(accountStorage, clock)
    }

    @Test
    fun `delete when accounts is absent then do nothing`() {
        val userId = 5.asId<User>()
        val ids = setOf("55".asId<Account>())

        every { accountStorage.findAllByUserIdAndIds(userId, ids) } returns emptySet()

        accountDeletionImpl.delete(userId, ids)

        verify(exactly = 1) { accountStorage.findAllByUserIdAndIds(userId, ids) }
        verify(exactly = 0) { accountStorage.setForRemovalAndClose(any(), any()) }
    }

    @Test
    fun `delete when accounts by provided ids exists then set that accounts for removal`() {
        val userId = 5.asId<User>()
        val accounts = setOf(
            account("1".asId(), userId) { status = Account.Status.FOR_REMOVAL },
            account("2".asId(), userId) { status = Account.Status.FOR_REMOVAL },
        )
        val ids = accounts.mapTo(HashSet()) { it.id }

        every { accountStorage.findAllByUserIdAndIds(userId, ids) } returns accounts

        accountDeletionImpl.delete(userId, ids)

        verify(exactly = 1) { accountStorage.findAllByUserIdAndIds(userId, ids) }
        verify(exactly = 1) { accountStorage.setForRemovalAndClose(ids, clock.date()) }
    }
}
