package com.marsofandrew.bookkeeper.spending.account

import com.marsofandrew.bookkeeper.account.AccountValidation
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.user.User
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AccountValidatorImplTest {

    private val accountValidation = mockk<AccountValidation>()

    private lateinit var accountValidatorImpl: AccountValidatorImpl

    @BeforeEach
    fun setup() {
        accountValidatorImpl = AccountValidatorImpl(accountValidation)
    }

    @Test
    fun `validate when account belongs to user returns true`() {
        val accountId = "test".asId<Account>()
        val userId = 456.asId<User>()

        every { accountValidation.validate(userId.value.asId(), accountId.value.asId()) } returns true

        val result = accountValidatorImpl.validate(userId, accountId)

        result shouldBe true
    }

    @Test
    fun `validate when account does not belong to user returns false`() {
        val accountId = "test".asId<Account>()
        val userId = 456.asId<User>()

        every { accountValidation.validate(userId.value.asId(), accountId.value.asId()) } returns false

        val result = accountValidatorImpl.validate(userId, accountId)

        result shouldBe false
    }
}
