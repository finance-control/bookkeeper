package com.marsofandrew.bookkeeper.report.vent.listener

import com.marsofandrew.bookkeeper.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.ReportSpendingAdding
import com.marsofandrew.bookkeeper.report.ReportSpendingRemoving
import com.marsofandrew.bookkeeper.report.fixture.spending
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
        ReportMoneyIsSpendEventListenerTest.ContextConfiguration::class
    ]
)
internal class ReportMoneyIsSpendEventListenerTest {

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher


    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `onMoneyIsSpent when MoneyIsSpendEvent is published adds spending to reports`() {
        val event = MoneyIsSpendEvent(
            userId = 5,
            date = LocalDate.now(),
            money = AccountBondedMoney(money = PositiveMoney(Currency.USD, 48, 1), accountId = null),
            category = 45
        )

        val expectedSpending = spending(event.userId.asId()) {
            date = event.date
            money = event.money.money
            spendingCategoryId = event.category.asId()
        }

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 1) { reportSpendingAdding.add(expectedSpending) }
    }

    @Test
    fun `onRollbackMoneyIsSpent when RollbackMoneyIsSpendEvent is published removes spending from reports`() {
        val event = RollbackMoneyIsSpendEvent(
            userId = 5,
            date = LocalDate.now(),
            money = AccountBondedMoney(money = PositiveMoney(Currency.USD, 48, 1), accountId = null),
            category = 45
        )

        val expectedSpending = spending(event.userId.asId()) {
            date = event.date
            money = event.money.money
            spendingCategoryId = event.category.asId()
        }

        applicationEventPublisher.publishEvent(event)

        verify(exactly = 1) { reportSpendingRemoving.remove(expectedSpending) }
    }

    @Configuration
    class ContextConfiguration {

        @Bean
        fun reportMoneyIsSpendEventListener() = ReportMoneyIsSpendEventListener(
            reportSpendingAdding,
            reportSpendingRemoving
        )
    }

    companion object {
        val reportSpendingAdding = mockk<ReportSpendingAdding>(relaxUnitFun = true)
        val reportSpendingRemoving = mockk<ReportSpendingRemoving>(relaxUnitFun = true)
    }
}