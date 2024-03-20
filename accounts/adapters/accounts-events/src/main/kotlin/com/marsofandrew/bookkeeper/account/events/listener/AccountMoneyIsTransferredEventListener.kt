package com.marsofandrew.bookkeeper.account.events.listener

import com.marsofandrew.bookkeeper.account.AccountMoneyTransferring
import com.marsofandrew.bookkeeper.account.RollbackAccountMoneyTransferring
import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.properties.id.asId
import org.apache.logging.log4j.LogManager
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
internal class AccountMoneyIsTransferredEventListener(
    private val accountMoneyTransferring: AccountMoneyTransferring,
    private val rollbackAccountMoneyTransferring: RollbackAccountMoneyTransferring
) {

    private val logger = LogManager.getLogger()

    @EventListener(MoneyIsTransferredEvent::class)
    fun onMoneyIsTransferred(event: MoneyIsTransferredEvent) {
        accountMoneyTransferring.transfer(
            userId = event.userId.asId(),
            from = event.send?.toAccountTransferAmount(),
            to = event.received.toAccountTransferAmount()
        )
        logger.info("MoneyIsTransferredEvent $event has been handled by accounts")
    }

    @EventListener(RollbackMoneyIsTransferredEvent::class)
    fun onRollbackMoneyIsTransferred(event: RollbackMoneyIsTransferredEvent) {
        rollbackAccountMoneyTransferring.rollbackTransfer(
            userId = event.userId.asId(),
            from = event.send?.toAccountTransferAmount(),
            to = event.received.toAccountTransferAmount()
        )
        logger.info("RollbackMoneyIsTransferredEvent $event has been handled by accounts")
    }
}
