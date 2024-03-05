package com.marsofandrew.bookkeeper.account.events.listener

import com.marsofandrew.bookkeeper.account.AccountMoneyTransferring
import com.marsofandrew.bookkeeper.account.RollbackAccountMoneyTransferring
import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.properties.id.asId
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
internal class MoneyIsTransferredEventListener(
    private val accountMoneyTransferring: AccountMoneyTransferring,
    private val rollbackAccountMoneyTransferring: RollbackAccountMoneyTransferring
) {

    @EventListener(MoneyIsTransferredEvent::class)
    fun onMoneyIsTransferred(event: MoneyIsTransferredEvent) {
        accountMoneyTransferring.transfer(
            userId = event.userId.asId(),
            from = event.send?.toAccountTransferAmount(),
            to = event.received.toAccountTransferAmount()
        )
    }

    @EventListener(RollbackMoneyIsTransferredEvent::class)
    fun onRollbackMoneyIsTransferred(event: MoneyIsTransferredEvent) {
        rollbackAccountMoneyTransferring.rollbackTransfer(
            userId = event.userId.asId(),
            from = event.send?.toAccountTransferAmount(),
            to = event.received.toAccountTransferAmount()
        )
    }
}
