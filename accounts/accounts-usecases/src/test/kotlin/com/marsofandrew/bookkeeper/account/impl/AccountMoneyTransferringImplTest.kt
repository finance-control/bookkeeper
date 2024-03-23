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

internal class AccountMoneyTransferringImplTest {

    private val userId = 5.asId<User>()

    private val accountStorage = mockk<AccountStorage>(relaxUnitFun = true)
    private val transactionExecutor = object : TransactionExecutor {
        override fun <T> execute(block: () -> T): T {
            return block()
        }
    }

    private lateinit var accountMoneyTransferringImpl: AccountMoneyTransferringImpl

    @BeforeEach
    fun setup() {
        accountMoneyTransferringImpl = AccountMoneyTransferringImpl(accountStorage, transactionExecutor)
    }

    @Test
    fun `transfer then source is null then topUps destination account`() {
        val account = account("5".asId(), userId) { money = Money(Currency.EUR, 5, 0) }

        every { accountStorage.findByUserIdAndIdOrThrow(userId, account.id) } returns account

        accountMoneyTransferringImpl.transfer(
            userId = userId,
            source = null,
            destination = AccountTransferAmount(
                accountId = account.id,
                money = PositiveMoney(Currency.EUR, 10, 1)
            )
        )

        verify(exactly = 1) { accountStorage.findByUserIdAndIdOrThrow(userId, account.id) }
        verify(exactly = 1) { accountStorage.setMoney(account.id, Money(Currency.EUR, 6, 0)) }
    }

    @Test
    fun `transfer then to is null then withdraws from source account`() {
        val account = account("5".asId(), userId) { money = Money(Currency.EUR, 5, 0) }

        every { accountStorage.findByUserIdAndIdOrThrow(userId, account.id) } returns account

        accountMoneyTransferringImpl.transfer(
            userId = userId,
            source = AccountTransferAmount(
                accountId = account.id,
                money = PositiveMoney(Currency.EUR, 10, 1)
            ),
            destination = null
        )

        verify(exactly = 1) { accountStorage.findByUserIdAndIdOrThrow(userId, account.id) }
        verify(exactly = 1) { accountStorage.setMoney(account.id, Money(Currency.EUR, 4, 0)) }
    }

    @Test
    fun `transfer when destination and source arguments are null then do nothing`() {
        accountMoneyTransferringImpl.transfer(
            userId = userId,
            source = null,
            destination = null
        )

        verify { accountStorage wasNot Called }
    }

    @Test
    fun `transfer when all parameters are set then withdraws from source account and topUps destinations account`() {
        val sourceAccount = account("5".asId(), userId) { money = Money(Currency.EUR, 5, 0) }
        val destinationAccount = account("6".asId(), userId) { money = Money(Currency.USD, 5, 0) }

        every { accountStorage.findByUserIdAndIdOrThrow(userId, sourceAccount.id) } returns sourceAccount
        every { accountStorage.findByUserIdAndIdOrThrow(userId, destinationAccount.id) } returns destinationAccount

        accountMoneyTransferringImpl.transfer(
            userId = userId,
            source = AccountTransferAmount(
                accountId = sourceAccount.id,
                money = PositiveMoney(Currency.EUR, 10, 1)
            ),
            destination =  AccountTransferAmount(
                accountId = destinationAccount.id,
                money = PositiveMoney(Currency.USD, 11, 1)
            )
        )

        verify(exactly = 1) { accountStorage.findByUserIdAndIdOrThrow(userId, sourceAccount.id) }
        verify(exactly = 1) { accountStorage.setMoney(sourceAccount.id, Money(Currency.EUR, 4, 0)) }
        verify(exactly = 1) { accountStorage.findByUserIdAndIdOrThrow(userId, destinationAccount.id) }
        verify(exactly = 1) { accountStorage.setMoney(destinationAccount.id, Money(Currency.USD, 61, 1)) }
    }

    @Test
    fun `transfer throws exception when destination account is absent`() {
        val id = "ss".asId<Account>()

        every { accountStorage.findByUserIdAndIdOrThrow(userId, id) } throws DomainModelNotFoundException(id)

        shouldThrowExactly<DomainModelNotFoundException> {
            accountMoneyTransferringImpl.transfer(
                userId = userId,
                source = null,
                destination = AccountTransferAmount(
                    accountId = id,
                    money = PositiveMoney(Currency.EUR, 10, 1)
                )
            )
        }

        verify(exactly = 0) { accountStorage.setMoney(any(), any()) }
    }

    @Test
    fun `transfer throws exception when source account is absent`() {
        val id = "ss".asId<Account>()

        every { accountStorage.findByUserIdAndIdOrThrow(userId, id) } throws DomainModelNotFoundException(id)

        shouldThrowExactly<DomainModelNotFoundException> {
            accountMoneyTransferringImpl.transfer(
                userId = userId,
                source = AccountTransferAmount(
                    accountId = id,
                    money = PositiveMoney(Currency.EUR, 10, 1)
                ),
                destination = null
            )
        }

        verify(exactly = 0) { accountStorage.setMoney(any(), any()) }
    }
}