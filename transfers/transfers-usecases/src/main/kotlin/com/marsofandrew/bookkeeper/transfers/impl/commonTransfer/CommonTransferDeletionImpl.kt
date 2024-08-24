package com.marsofandrew.bookkeeper.transfers.impl.commonTransfer

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.CommonTransferDeletion
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.impl.utils.toRollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.transfers.user.User
import org.apache.logging.log4j.LogManager

class CommonTransferDeletionImpl(
    private val transferStorage: TransferStorage,
    private val eventPublisher: EventPublisher,
) : CommonTransferDeletion {

    private val logger = LogManager.getLogger()

    override fun delete(userId: NumericId<User>, ids: Collection<NumericId<CommonTransfer>>) {
        val transfers = transferStorage.findAllByUserIdAndIds(userId, ids)
        transferStorage.delete(transfers.map { it.id })
        logger.info("CommonTransfers with ids: ${transfers.map { it.id }} were deleted")

        eventPublisher.publish(transfers.map { it.toRollbackMoneyIsTransferredEvent() })
    }
}


