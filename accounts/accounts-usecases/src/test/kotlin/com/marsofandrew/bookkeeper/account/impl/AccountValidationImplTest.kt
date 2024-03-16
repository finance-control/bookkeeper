package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AccountValidationImplTest {

    private val accountStorage = mockk<AccountStorage>()

    private lateinit var accountValidationImpl: AccountValidationImpl

    @BeforeEach
    fun setup() {
        accountValidationImpl = AccountValidationImpl(accountStorage)
    }

    @Test
    fun `validate when account belongs to user returns true`() {
        val accountId = "test".asId<Account>()
        val userId = 456.asId<User>()

        every { accountStorage.existsByUserIdAndAccountId(userId, accountId) } returns true

        val result = accountValidationImpl.validate(userId, accountId)

        result shouldBe true
    }

    @Test
    fun `validate when account does not belong to user returns false`() {
        val accountId = "test".asId<Account>()
        val userId = 456.asId<User>()

        every { accountStorage.existsByUserIdAndAccountId(userId, accountId) } returns false

        val result = accountValidationImpl.validate(userId, accountId)

        result shouldBe false
    }
}
