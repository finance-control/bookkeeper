package com.marsofandrew.bookkeeper.transfer.impl

import com.marsofandrew.bookkeeper.event.RollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.TransferDeletion
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.impl.utils.toAccountBoundedMoney
import com.marsofandrew.bookkeeper.transfer.user.User
import org.apache.logging.log4j.LogManager

class TransferDeletionImpl(
    private val transferStorage: TransferStorage,
    private val eventPublisher: EventPublisher,
) : TransferDeletion {

    private val logger = LogManager.getLogger()

    override fun delete(userId: NumericId<User>, ids: Collection<NumericId<Transfer>>) {
        val transfers = transferStorage.findAllByUserIdAndIds(userId, ids)
        transferStorage.delete(transfers.map { it.id })
        logger.info("Transfers with ids: ${transfers.map { it.id }} were deleted")

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
