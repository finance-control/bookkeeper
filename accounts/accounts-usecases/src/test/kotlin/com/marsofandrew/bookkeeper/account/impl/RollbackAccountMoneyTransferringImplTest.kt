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
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RollbackAccountMoneyTransferringImplTest {

    private val userId = 5.asId<User>()

    private val accountStorage = mockk<AccountStorage>(relaxUnitFun = true)
    private val transactionExecutor = object : TransactionExecutor {
        override fun <T> execute(block: () -> T): T {
            return block()
        }
    }

    private lateinit var rollbackAccountMoneyTransferringImpl: RollbackAccountMoneyTransferringImpl

    @BeforeEach
    fun setup() {
        rollbackAccountMoneyTransferringImpl = RollbackAccountMoneyTransferringImpl(accountStorage, transactionExecutor)
    }

    @Test
    fun `rollbackTransfer then from is null then withdraws from 'to' account`() {
        val account = account("5".asId(), userId) { money = Money(Currency.EUR, 5, 0) }

        every { accountStorage.findByUserIdAndIdOrThrow(userId, account.id) } returns account

        rollbackAccountMoneyTransferringImpl.rollbackTransfer(
            userId = userId,
            from = null,
            to = AccountTransferAmount(
                accountId = account.id,
                money = PositiveMoney(Currency.EUR, 10, 1)
            )
        )

        verify(exactly = 1) { accountStorage.findByUserIdAndIdOrThrow(userId, account.id) }
        verify(exactly = 1) { accountStorage.setMoney(account.id, Money(Currency.EUR, 4, 0)) }
    }

    @Test
    fun `rollbackTransfer then to is null then topUps 'from' account`() {
        val account = account("5".asId(), userId) { money = Money(Currency.EUR, 5, 0) }

        every { accountStorage.findByUserIdAndIdOrThrow(userId, account.id) } returns account

        rollbackAccountMoneyTransferringImpl.rollbackTransfer(
            userId = userId,
            from = AccountTransferAmount(
                accountId = account.id,
                money = PositiveMoney(Currency.EUR, 10, 1)
            ),
            to = null
        )

        verify(exactly = 1) { accountStorage.findByUserIdAndIdOrThrow(userId, account.id) }
        verify(exactly = 1) { accountStorage.setMoney(account.id, Money(Currency.EUR, 6, 0)) }
    }

    @Test
    fun `rollbackTransfer when 'to' and 'from' arguments are null then do nothing`() {
        rollbackAccountMoneyTransferringImpl.rollbackTransfer(
            userId = userId,
            from = null,
            to = null
        )

        verify { accountStorage wasNot Called }
    }

    @Test
    fun `rollbackTransfer when all parameters are set then topUps 'from' account and withdraws from 'to' account`() {
        val fromAccount = account("5".asId(), userId) { money = Money(Currency.EUR, 5, 0) }
        val toAccount = account("6".asId(), userId) { money = Money(Currency.USD, 5, 0) }

        every { accountStorage.findByUserIdAndIdOrThrow(userId, fromAccount.id) } returns fromAccount
        every { accountStorage.findByUserIdAndIdOrThrow(userId, toAccount.id) } returns toAccount

        rollbackAccountMoneyTransferringImpl.rollbackTransfer(
            userId = userId,
            from = AccountTransferAmount(
                accountId = fromAccount.id,
                money = PositiveMoney(Currency.EUR, 10, 1)
            ),
            to =  AccountTransferAmount(
                accountId = toAccount.id,
                money = PositiveMoney(Currency.USD, 11, 1)
            )
        )

        verify(exactly = 1) { accountStorage.findByUserIdAndIdOrThrow(userId, fromAccount.id) }
        verify(exactly = 1) { accountStorage.setMoney(fromAccount.id, Money(Currency.EUR, 6, 0)) }
        verify(exactly = 1) { accountStorage.findByUserIdAndIdOrThrow(userId, toAccount.id) }
        verify(exactly = 1) { accountStorage.setMoney(toAccount.id, Money(Currency.USD, 39, 1)) }
    }

    @Test
    fun `rollbackTransfer throws exception when account from 'to' account is absent`() {
        val id = "ss".asId<Account>()

        every { accountStorage.findByUserIdAndIdOrThrow(userId, id) } throws DomainModelNotFoundException(id)

        shouldThrowExactly<DomainModelNotFoundException> {
            rollbackAccountMoneyTransferringImpl.rollbackTransfer(
                userId = userId,
                from = null,
                to = AccountTransferAmount(
                    accountId = id,
                    money = PositiveMoney(Currency.EUR, 10, 1)
                )
            )
        }

        verify(exactly = 0) { accountStorage.setMoney(any(), any()) }
    }

    @Test
    fun `rollbackTransfer throws exception when account from 'from' account is absent`() {
        val id = "ss".asId<Account>()

        every { accountStorage.findByUserIdAndIdOrThrow(userId, id) } throws DomainModelNotFoundException(id)

        shouldThrowExactly<DomainModelNotFoundException> {
            rollbackAccountMoneyTransferringImpl.rollbackTransfer(
                userId = userId,
                from = AccountTransferAmount(
                    accountId = id,
                    money = PositiveMoney(Currency.EUR, 10, 1)
                ),
                to = null
            )
        }

        verify(exactly = 0) { accountStorage.setMoney(any(), any()) }
    }
}