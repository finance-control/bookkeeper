package com.marsofandrew.bookkeeper.accounts.impl

import com.marsofandrew.bookkeeper.accounts.Account
import com.marsofandrew.bookkeeper.accounts.access.AccountStorage
import com.marsofandrew.bookkeeper.accounts.fixtures.account
import com.marsofandrew.bookkeeper.accounts.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.base.exception.DomainModelNotFoundException
import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
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

internal class RollBackAccountMoneySpendingImplTest {

    private val userId = 5.asId<User>()

    private val accountStorage = mockk<AccountStorage>(relaxUnitFun = true)
    private val transactionalExecution = object : TransactionalExecution {
        override fun <T> execute(block: () -> T): T {
            return block()
        }
    }

    private lateinit var rollbackAccountMoneySpendingImpl: RollbackAccountMoneySpendingImpl

    @BeforeEach
    fun setup() {
        rollbackAccountMoneySpendingImpl = RollbackAccountMoneySpendingImpl(accountStorage, transactionalExecution)
    }

    @Test
    fun `rollbackSpending throws exception when account is absent`() {
        val accountId = "5".asId<Account>()
        every { accountStorage.findByUserIdAndIdOrThrow(userId, accountId) } throws DomainModelNotFoundException(
            accountId
        )

        shouldThrowExactly<DomainModelNotFoundException> {
            rollbackAccountMoneySpendingImpl.rollbackSpending(
                userId, AccountTransferAmount(
                    accountId = accountId,
                    money = PositiveMoney(Currency.EUR, 5, 4)
                )
            )
        }
    }

    @Test
    fun `rollbackSpending withdraws money when account exists`() {
        val account = account("5".asId(), userId) { money = Money(Currency.EUR, 5000, 2) }

        every { accountStorage.findByUserIdAndIdOrThrow(userId, account.id) } returns account

        rollbackAccountMoneySpendingImpl.rollbackSpending(
            userId,
            AccountTransferAmount(
                accountId = account.id,
                money = PositiveMoney(Currency.EUR, 10, 1)
            )
        )

        verify(exactly = 1) { accountStorage.setMoney(account.id, Money(Currency.EUR, 51, 0)) }
    }
}