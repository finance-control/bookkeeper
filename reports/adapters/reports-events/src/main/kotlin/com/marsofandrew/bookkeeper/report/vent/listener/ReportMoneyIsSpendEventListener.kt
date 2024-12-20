package com.marsofandrew.bookkeeper.report.vent.listener

import com.marsofandrew.bookkeeper.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.ReportSpendingAdding
import com.marsofandrew.bookkeeper.report.ReportSpendingRemoving
import com.marsofandrew.bookkeeper.report.spending.Spending
import org.apache.logging.log4j.LogManager
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
internal class ReportMoneyIsSpendEventListener(
    private val reportSpendingAdding: ReportSpendingAdding,
    private val reportSpendingRemoving: ReportSpendingRemoving
) {

    private val logger = LogManager.getLogger()

    @EventListener(MoneyIsSpendEvent::class)
    fun onMoneyIsSpent(event: MoneyIsSpendEvent) {
        val spending = Spending(
            userId = event.userId.asId(),
            money = event.money.money,
            date = event.date,
            categoryId = event.category.asId()
        )

        reportSpendingAdding.add(spending)
        logger.info("MoneyIsSpendEvent $event was handled by Reports")
    }

    @EventListener(RollbackMoneyIsSpendEvent::class)
    fun onRollbackMoneyIsSpent(event: RollbackMoneyIsSpendEvent) {
        val spending = Spending(
            userId = event.userId.asId(),
            money = event.money.money,
            date = event.date,
            categoryId = event.category.asId()
        )

        reportSpendingRemoving.remove(spending)

        logger.info("RollbackMoneyIsSpendEvent $event was handled by Reports")
    }
}