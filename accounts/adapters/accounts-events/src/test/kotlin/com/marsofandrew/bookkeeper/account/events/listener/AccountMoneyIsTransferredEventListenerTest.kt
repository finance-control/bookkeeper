package com.marsofandrew.bookkeeper.account.events.listener

import com.marsofandrew.bookkeeper.account.AccountMoneyTransferring
import com.marsofandrew.bookkeeper.account.RollbackAccountMoneyTransferring
import com.marsofandrew.bookkeeper.account.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsTransferredEvent
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
        AccountMoneyIsTransferredEventListenerTest.ContextConfiguration::class,
        ApplicationEventPublisher::class
    ]
)
internal class AccountMoneyIsTransferredEventListenerTest {

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `onMoneyIsTransferred when MoneyIsTransferred event with account is published then calls spend`() {
        val userId = 458.asId<User>()
        val receivedTransferAmount = AccountTransferAmount(
            accountId = "jash".asId(),
            money = PositiveMoney(Currency.EUR, 45, 1)
        )

        val event = MoneyIsTransferredEvent(
            userId = userId.value,
            date = LocalDate.now(),
            send = null,
            received = AccountBondedMoney(money = receivedTransferAmount.money, receivedTransferAmount.accountId.value),
            category = 54
        )

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 1) { accountMoneyTransferring.transfer(userId, null, receivedTransferAmount) }
    }

    @Test
    fun `onRollbackMoneyIsTransferred when RollbackMoneyIsTransferred event with account is published then calls rollbackSpend`() {
        val userId = 458.asId<User>()
        val receivedTransferAmount = AccountTransferAmount(
            accountId = "jash".asId(),
            money = PositiveMoney(Currency.EUR, 45, 1)
        )

        val event = RollbackMoneyIsTransferredEvent(
            userId = userId.value,
            date = LocalDate.now(),
            send = null,
            received = AccountBondedMoney(money = receivedTransferAmount.money, receivedTransferAmount.accountId.value),
            category = 54
        )

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 1) {
            rollbackAccountMoneyTransferring.rollbackTransfer(userId, null, receivedTransferAmount)
        }
    }

    class ContextConfiguration {

        @Bean
        fun accountMoneyIsTransferredEventListener(): AccountMoneyIsTransferredEventListener =
            AccountMoneyIsTransferredEventListener(
                accountMoneyTransferring,
                rollbackAccountMoneyTransferring
            )
    }

    companion object {
        val accountMoneyTransferring = mockk<AccountMoneyTransferring>(relaxUnitFun = true)
        val rollbackAccountMoneyTransferring = mockk<RollbackAccountMoneyTransferring>(relaxUnitFun = true)
    }
}
