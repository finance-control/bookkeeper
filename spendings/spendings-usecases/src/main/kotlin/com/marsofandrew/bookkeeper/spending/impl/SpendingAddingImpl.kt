package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.SpendingAdding

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