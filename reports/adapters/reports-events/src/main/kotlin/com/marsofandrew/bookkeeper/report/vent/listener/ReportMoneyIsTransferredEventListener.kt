package com.marsofandrew.bookkeeper.report.vent.listener

import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.ReportEarningAdding
import com.marsofandrew.bookkeeper.report.ReportEarningRemoving
import com.marsofandrew.bookkeeper.report.ReportTransferAdding
import com.marsofandrew.bookkeeper.report.ReportTransferRemoving
import com.marsofandrew.bookkeeper.report.earning.Earning
import com.marsofandrew.bookkeeper.report.transfer.Transfer
import org.apache.logging.log4j.LogManager
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
internal class ReportMoneyIsTransferredEventListener(
    private val reportTransferAdding: ReportTransferAdding,
    private val reportEarningAdding: ReportEarningAdding,
    private val reportTransferRemoving: ReportTransferRemoving,
    private val reportEarningRemoving: ReportEarningRemoving
) {

    private val logger = LogManager.getLogger()

    @EventListener(MoneyIsTransferredEvent::class)
    fun onMoneyIsTransferred(event: MoneyIsTransferredEvent) {
        val transfer = event.toTransfer()
        val earning = event.toEarning()

        transfer?.let { reportTransferAdding.add(it) }
        earning?.let { reportEarningAdding.add(it) }

        logger.info("MoneyIsTransferredEvent $event was handled by Reports")
    }

    @EventListener(RollbackMoneyIsTransferredEvent::class)
    fun onRollbackMoneyIsTransferred(event: RollbackMoneyIsTransferredEvent) {
        val transfer = event.toTransfer()
        val earning = event.toEarning()

        transfer?.let { reportTransferRemoving.remove(it) }
        earning?.let { reportEarningRemoving.remove(it) }

        logger.info("RollbackMoneyIsTransferredEvent $event was handled by Reports")
    }

    private fun MoneyIsTransferredEvent.toTransfer(): Transfer? =
        send?.let {
            Transfer(
                userId = userId.asId(),
                date = date,
                send = it.money,
                received = received.money,
                categoryId = category.asId()
            )
        }

    private fun MoneyIsTransferredEvent.toEarning(): Earning? =
        if (send == null) {
            Earning(
                userId = userId.asId(),
                date = date,
                money = received.money,
                categoryId = category.asId()
            )
        } else null

    private fun RollbackMoneyIsTransferredEvent.toTransfer(): Transfer? =
        send?.let {
            Transfer(
                userId = userId.asId(),
                date = date,
                send = it.money,
                received = received.money,
                categoryId = category.asId()
            )
        }

    private fun RollbackMoneyIsTransferredEvent.toEarning(): Earning? =
        if (send == null) {
            Earning(
                userId = userId.asId(),
                date = date,
                money = received.money,
                categoryId = category.asId()
            )
        } else null
}