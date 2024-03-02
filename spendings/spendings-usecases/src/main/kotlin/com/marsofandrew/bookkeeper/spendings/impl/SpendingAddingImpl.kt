package com.marsofandrew.bookkeeper.spendings.impl

import com.marsofandrew.bookkeeper.events.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.events.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.events.publisher.EventPublisher
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.access.SpendingStorage
import com.marsofandrew.bookkeeper.spendings.SpendingAdding

class SpendingAddingImpl(
    private val spendingStorage: SpendingStorage,
    private val eventPublisher: EventPublisher
) : SpendingAdding {

    override fun add(spending: Spending): Spending {
        val createdSpending = spendingStorage.create(spending)
        eventPublisher.publish(createdSpending.toMoneyIsSpendEvent())
        return createdSpending
    }
}

private fun Spending.toMoneyIsSpendEvent() = MoneyIsSpendEvent(
    userId = userId.value,
    date = date,
    money = AccountBondedMoney(money, fromAccount?.value),
    category = spendingCategoryId.value
)