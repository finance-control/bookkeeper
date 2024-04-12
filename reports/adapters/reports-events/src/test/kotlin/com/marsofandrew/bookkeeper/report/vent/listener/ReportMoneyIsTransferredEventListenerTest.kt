package com.marsofandrew.bookkeeper.report.vent.listener

import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.ReportEarningAdding
import com.marsofandrew.bookkeeper.report.ReportEarningRemoving
import com.marsofandrew.bookkeeper.report.ReportTransferAdding
import com.marsofandrew.bookkeeper.report.ReportTransferRemoving
import com.marsofandrew.bookkeeper.report.fixture.earning
import com.marsofandrew.bookkeeper.report.fixture.transfer
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
import org.springframework.context.annotation.Configuration

@SpringBootTest(
    classes = [
        ApplicationEventPublisher::class,
        ReportMoneyIsTransferredEventListenerTest.ContextConfiguration::class
    ]
)
internal class ReportMoneyIsTransferredEventListenerTest {

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher


    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `onMoneyIsTransferred when MoneyIsTransferredEvent with earning is published adds earning to reports`() {
        val event = MoneyIsTransferredEvent(
            userId = 5,
            date = LocalDate.now(),
            send = null,
            received = AccountBondedMoney(money = PositiveMoney(Currency.USD, 48, 1), accountId = null),
            category = 45
        )

        val expectedEarning = earning(event.userId.asId()) {
            date = event.date
            money = event.received.money
            categoryId = event.category.asId()
        }

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 1) { reportEarningAdding.add(expectedEarning) }
    }

    @Test
    fun `onRollbackMoneyIsTransferred when RollbackMoneyIsTransferredEvent with earning is published removes earning from reports`() {
        val event = RollbackMoneyIsTransferredEvent(
            userId = 5,
            date = LocalDate.now(),
            send = null,
            received = AccountBondedMoney(money = PositiveMoney(Currency.USD, 48, 1), accountId = null),
            category = 45
        )

        val expectedEarning = earning(event.userId.asId()) {
            date = event.date
            money = event.received.money
            categoryId = event.category.asId()
        }

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 1) { reportEarningRemoving.remove(expectedEarning) }
    }

    @Test
    fun `onMoneyIsTransferred when MoneyIsTransferredEvent with transfer is published adds transfer to reports`() {
        val event = MoneyIsTransferredEvent(
            userId = 5,
            date = LocalDate.now(),
            send = AccountBondedMoney(money = PositiveMoney(Currency.EUR, 87, 1), accountId = null),
            received = AccountBondedMoney(money = PositiveMoney(Currency.USD, 48, 1), accountId = null),
            category = 45
        )

        val expectedTransfer = transfer(event.userId.asId()) {
            date = event.date
            send = event.send!!.money
            received = event.received.money
            categoryId = event.category.asId()
        }

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 1) { reportTransferAdding.add(expectedTransfer) }
    }

    @Test
    fun `onRollbackMoneyIsTransferred when RollbackMoneyIsTransferredEvent with transfer is published removes transfer from reports`() {
        val event = RollbackMoneyIsTransferredEvent(
            userId = 5,
            date = LocalDate.now(),
            send = AccountBondedMoney(money = PositiveMoney(Currency.EUR, 87, 1), accountId = null),
            received = AccountBondedMoney(money = PositiveMoney(Currency.USD, 48, 1), accountId = null),
            category = 45
        )

        val expectedTransfer = transfer(event.userId.asId()) {
            date = event.date
            send = event.send!!.money
            received = event.received.money
            categoryId = event.category.asId()
        }

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 1) { reportTransferRemoving.remove(expectedTransfer) }
    }

    @Configuration
    class ContextConfiguration {

        @Bean
        fun reportMoneyIsTransferredEventListener() = ReportMoneyIsTransferredEventListener(
            reportTransferAdding,
            reportEarningAdding,
            reportTransferRemoving,
            reportEarningRemoving
        )
    }

    companion object {
        val reportEarningAdding = mockk<ReportEarningAdding>(relaxUnitFun = true)
        val reportEarningRemoving = mockk<ReportEarningRemoving>(relaxUnitFun = true)
        val reportTransferAdding = mockk<ReportTransferAdding>(relaxUnitFun = true)
        val reportTransferRemoving = mockk<ReportTransferRemoving>(relaxUnitFun = true)
    }
}