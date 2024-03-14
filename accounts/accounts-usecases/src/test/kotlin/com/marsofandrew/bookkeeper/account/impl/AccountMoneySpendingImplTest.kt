package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.fixtures.account
import com.marsofandrew.bookkeeper.account.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.exception.DomainModelNotFoundException
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AccountMoneySpendingImplTest {

    private val userId = 5.asId<User>()

    private val accountStorage = mockk<AccountStorage>(relaxUnitFun = true)
    private val transactionExecutor = object : TransactionExecutor {
        override fun <T> execute(block: () -> T): T {
            return block()
        }
    }

    private lateinit var accountMoneySpendingImpl: AccountMoneySpendingImpl

    @BeforeEach
    fun setup() {
        accountMoneySpendingImpl = AccountMoneySpendingImpl(accountStorage, transactionExecutor)
    }

    @Test
    fun `spend throws exception when account is absent`() {
        val accountId = "5".asId<Account>()
        every { accountStorage.findByUserIdAndIdOrThrow(userId, accountId) } throws DomainModelNotFoundException(
            accountId
        )

        shouldThrowExactly<DomainModelNotFoundException> {
            accountMoneySpendingImpl.spend(
                userId, AccountTransferAmount(
                    accountId = accountId,
                    money = PositiveMoney(Currency.EUR, 5, 4)
                )
            )
        }
    }

    @Test
    fun `spend withdraws money when account exists`() {
        val account = account("5".asId(), userId) { money = Money(Currency.EUR, 5000, 2) }

        every { accountStorage.findByUserIdAndIdOrThrow(userId, account.id) } returns account

        accountMoneySpendingImpl.spend(
            userId,
            AccountTransferAmount(
                accountId = account.id,
                money = PositiveMoney(Currency.EUR, 10, 1)
            )
        )

        verify(exactly = 1) { accountStorage.setMoney(account.id, Money(Currency.EUR, 49, 0)) }
    }
}