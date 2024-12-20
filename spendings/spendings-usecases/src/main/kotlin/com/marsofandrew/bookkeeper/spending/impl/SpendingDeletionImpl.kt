package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingDeletion
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.impl.util.toRollbackMoneyIsSendEvent
import com.marsofandrew.bookkeeper.spending.user.User
import org.apache.logging.log4j.LogManager

class SpendingDeletionImpl(
    private val spendingStorage: SpendingStorage,
    private val eventPublisher: EventPublisher,
) : SpendingDeletion {

    private val logger = LogManager.getLogger()

    override fun delete(userId: NumericId<User>, ids: Collection<NumericId<Spending>>) {
        val spendings = spendingStorage.findAllByUserIdAndIds(userId, ids)
        spendingStorage.delete(spendings.map { it.id })
        logger.info("Spendings with id: ${spendings.map { it.id }} were deleted")
        eventPublisher.publish(spendings.map { it.toRollbackMoneyIsSendEvent() })
    }
}
