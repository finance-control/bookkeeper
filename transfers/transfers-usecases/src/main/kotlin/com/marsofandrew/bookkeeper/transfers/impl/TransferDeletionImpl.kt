package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.events.event.RollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.events.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.TransferDeletion
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.impl.utils.toAccountBoundedMoney
import com.marsofandrew.bookkeeper.transfers.user.User

class TransferDeletionImpl(
    private val transferStorage: TransferStorage,
    private val eventPublisher: EventPublisher,
) : TransferDeletion {

    override fun delete(userId: NumericId<User>, ids: Collection<NumericId<Transfer>>) {
        val transfers = transferStorage.findAllByUserIdAndIds(userId, ids)
        transferStorage.delete(transfers.map { it.id })
        eventPublisher.publish(transfers.map { it.toRollbackMoneyIsTransferredEvent() })
    }
}

private fun Transfer.toRollbackMoneyIsTransferredEvent() = RollbackMoneyIsTransferredEvent(
    userId = userId.value,
    date = date,
    send = send?.toAccountBoundedMoney(),
    received = received.toAccountBoundedMoney(),
    category = transferCategoryId.value
)
