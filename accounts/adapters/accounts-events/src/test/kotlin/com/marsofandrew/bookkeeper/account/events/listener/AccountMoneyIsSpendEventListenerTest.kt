package com.marsofandrew.bookkeeper.account.events.listener

import com.marsofandrew.bookkeeper.account.AccountMoneySpending
import com.marsofandrew.bookkeeper.account.RollbackAccountMoneySpending
import com.marsofandrew.bookkeeper.account.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean

@SpringBootTest(
    classes = [
        AccountMoneyIsSpendEventListenerTest.ContextConfiguration::class,
        ApplicationEventPublisher::class
    ]
)
internal class AccountMoneyIsSpendEventListenerTest {

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `onMoneyIsSpend when MoneyIsSpend event with account is published then calls spend`() {
        val userId = 458.asId<User>()
        val accountTransferAmount = AccountTransferAmount(
            accountId = "jash".asId(),
            money = PositiveMoney(Currency.EUR, 45, 1)
        )

        val event = MoneyIsSpendEvent(
            userId = userId.value,
            date = LocalDate.now(),
            money = AccountBondedMoney(money = accountTransferAmount.money, accountTransferAmount.accountId.value),
            category = 54
        )

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 1) { accountMoneySpending.spend(userId, accountTransferAmount) }
    }

    @Test
    fun `onMoneyIsSpend when MoneyIsSpend event is published without account then do nothing`() {
        val userId = 458.asId<User>()
        val accountTransferAmount = AccountTransferAmount(
            accountId = "jash".asId(),
            money = PositiveMoney(Currency.EUR, 45, 1)
        )

        val event = MoneyIsSpendEvent(
            userId = userId.value,
            date = LocalDate.now(),
            money = AccountBondedMoney(money = accountTransferAmount.money, null),
            category = 54
        )

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 0) { accountMoneySpending.spend(any(), any()) }
    }

    @Test
    fun `onRollbackMoneyIsSpend when RollbackMoneyIsSpend  event with account is published then calls rollbackSpend`() {
        val userId = 458.asId<User>()
        val accountTransferAmount = AccountTransferAmount(
            accountId = "jash".asId(),
            money = PositiveMoney(Currency.EUR, 45, 1)
        )

        val event = RollbackMoneyIsSpendEvent(
            userId = userId.value,
            date = LocalDate.now(),
            money = AccountBondedMoney(money = accountTransferAmount.money, accountTransferAmount.accountId.value),
            category = 54
        )

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 1) { rollbackAccountMoneySpending.rollbackSpending(userId, accountTransferAmount) }
    }

    @Test
    fun `onRollbackMoneyIsSpend when RollbackMoneyIsSpend  event is published without account then do nothing`() {
        val userId = 458.asId<User>()
        val accountTransferAmount = AccountTransferAmount(
            accountId = "jash".asId(),
            money = PositiveMoney(Currency.EUR, 45, 1)
        )

        val event = RollbackMoneyIsSpendEvent(
            userId = userId.value,
            date = LocalDate.now(),
            money = AccountBondedMoney(money = accountTransferAmount.money, null),
            category = 54
        )

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 0) { rollbackAccountMoneySpending.rollbackSpending(any(), any()) }
    }

    class ContextConfiguration {

        @Bean
        fun accountMoneyIsSpendEventListener(): AccountMoneyIsSpendEventListener = AccountMoneyIsSpendEventListener(
            accountMoneySpending,
            rollbackAccountMoneySpending
        )
    }

    companion object {
        val accountMoneySpending = mockk<AccountMoneySpending>(relaxUnitFun = true)
        val rollbackAccountMoneySpending = mockk<RollbackAccountMoneySpending>(relaxUnitFun = true)
    }
}
