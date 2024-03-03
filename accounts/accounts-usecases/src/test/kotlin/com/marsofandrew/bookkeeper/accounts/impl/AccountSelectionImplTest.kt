package com.marsofandrew.bookkeeper.accounts.impl

import com.marsofandrew.bookkeeper.accounts.Account
import com.marsofandrew.bookkeeper.accounts.access.AccountStorage
import com.marsofandrew.bookkeeper.accounts.fixtures.account
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AccountSelectionImplTest {

    private val accountStorage = mockk<AccountStorage>()

    private lateinit var accountSelectionImpl: AccountSelectionImpl

    @BeforeEach
    fun setup() {
        accountSelectionImpl = AccountSelectionImpl(accountStorage)
    }

    @Test
    fun `select returns accounts that are not set for removal`() {
        val userId = 5.asId<User>()

        val accounts = setOf(
            account("1".asId(), userId),
            account("2".asId(), userId),
            account("3".asId(), userId),
        )

        val accountsForRemoval = setOf(
            account("5".asId(), userId) { status = Account.Status.FOR_REMOVAL }
        )

        every { accountStorage.findAllByUserId(userId) } returns accounts + accountsForRemoval

        val result = accountSelectionImpl.select(userId)

        result shouldContainExactlyInAnyOrder accounts
    }
}
