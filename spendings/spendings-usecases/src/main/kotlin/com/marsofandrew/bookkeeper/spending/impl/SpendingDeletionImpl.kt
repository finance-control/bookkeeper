package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingDeletion
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.user.User

class SpendingDeletionImpl(
    private val spendingStorage: SpendingStorage,
    private val eventPublisher: EventPublisher,
) : SpendingDeletion {

    override fun delete(userId: NumericId<User>, ids: Collection<NumericId<Spending>>) {
        val spendings = spendingStorage.findAllByUserIdAndIds(userId, ids)
        spendingStorage.delete(spendings.map { it.id })
        eventPublisher.publish(spendings.map { it.toRollbackMoneyIsSendEvent() })
    }
}

private fun Spending.toRollbackMoneyIsSendEvent() = RollbackMoneyIsSpendEvent(
    userId = userId.value,
    date = date,
    money = AccountBondedMoney(money, fromAccount?.value),
    category = spendingCategoryId.value
)
