package com.marsofandrew.bookkeeper.account.events.listener

import com.marsofandrew.bookkeeper.account.AccountMoneySpending
import com.marsofandrew.bookkeeper.account.RollbackAccountMoneySpending
import com.marsofandrew.bookkeeper.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.properties.id.asId
import org.apache.logging.log4j.LogManager
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
internal class AccountMoneyIsSpendEventListener(
    private val accountMoneySpending: AccountMoneySpending,
    private val rollbackAccountMoneySpendingImpl: RollbackAccountMoneySpending
) {

    private val logger = LogManager.getLogger()

    @EventListener(MoneyIsSpendEvent::class)
    fun onMoneyIsSpend(event: MoneyIsSpendEvent) {
        event.money.toAccountTransferAmount()?.let {
            accountMoneySpending.spend(
                userId = event.userId.asId(),
                source = it
            )
        }

        logger.info("MoneyIsSpendEvent $event has been handled by accounts")
    }

    @EventListener(RollbackMoneyIsSpendEvent::class)
    fun onRollbackMoneyIsSpend(event: RollbackMoneyIsSpendEvent) {
        event.money.toAccountTransferAmount()?.let {
            rollbackAccountMoneySpendingImpl.rollbackSpending(
                userId = event.userId.asId(),
                source = it
            )
        }
        logger.info("RollbackMoneyIsSpendEvent $event has been handled by accounts")
    }
}
