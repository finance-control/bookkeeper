package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.fixtures.account
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AccountCreationImplTest {

    private val accountStorage = mockk<AccountStorage>(relaxUnitFun = true)

    private lateinit var accountCreationImpl: AccountCreationImpl

    @BeforeEach
    fun setup() {
        accountCreationImpl = AccountCreationImpl(accountStorage)
    }

    @Test
    fun `create creates storage`() {
        val accountToCreate = account(StringId.unidentified(), 5.asId())
        val identifiedAcc = accountToCreate.copy(id = "1".asId())
        every { accountStorage.create(accountToCreate) } returns identifiedAcc

        val result = accountCreationImpl.create(accountToCreate)

        result shouldBe identifiedAcc
    }
}