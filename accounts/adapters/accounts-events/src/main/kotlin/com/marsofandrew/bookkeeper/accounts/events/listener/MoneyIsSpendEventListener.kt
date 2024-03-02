package com.marsofandrew.bookkeeper.accounts.events.listener

import com.marsofandrew.bookkeeper.accounts.AccountMoneySpending
import com.marsofandrew.bookkeeper.accounts.RollbackAccountMoneySpending
import com.marsofandrew.bookkeeper.events.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.events.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.properties.id.asId
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
internal class MoneyIsSpendEventListener(
    private val accountMoneySpending: AccountMoneySpending,
    private val rollbackAccountMoneySpendingImpl: RollbackAccountMoneySpending
) {

    @EventListener(MoneyIsSpendEvent::class)
    fun onMoneyIsSpend(event: MoneyIsSpendEvent) {
        event.money.toAccountTransferAmount()?.let {
            accountMoneySpending.spend(
                userId = event.userId.asId(),
                from = it
            )
        }
    }

    @EventListener(RollbackMoneyIsSpendEvent::class)
    fun onRollbackMoneyIsSpend(event: RollbackMoneyIsSpendEvent) {
        event.money.toAccountTransferAmount()?.let {
            rollbackAccountMoneySpendingImpl.rollbackSpending(
                userId = event.userId.asId(),
                from = it
            )
        }
    }
}
