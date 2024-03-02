package com.marsofandrew.bookkeeper.spendings.impl

import com.marsofandrew.bookkeeper.events.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.events.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.events.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.SpendingDeletion
import com.marsofandrew.bookkeeper.spendings.access.SpendingStorage
import com.marsofandrew.bookkeeper.spendings.user.User

class SpendingDeletionImpl(
    private val spendingStorage: SpendingStorage,
    private val eventPublisher: EventPublisher,
) : SpendingDeletion {

    override fun delete(userId: NumericId<User>, ids: Collection<NumericId<Spending>>) {
        val spendings = spendingStorage.findAllByUserIdAndIds(userId, ids)
        spendingStorage.delete(ids)
        eventPublisher.publish(spendings.map { it.toRollbackMoneyIsSendEvent() })
    }
}

private fun Spending.toRollbackMoneyIsSendEvent() = RollbackMoneyIsSpendEvent(
    userId = userId.value,
    date = date,
    money = AccountBondedMoney(money, fromAccount?.value),
    category = spendingCategoryId.value
)