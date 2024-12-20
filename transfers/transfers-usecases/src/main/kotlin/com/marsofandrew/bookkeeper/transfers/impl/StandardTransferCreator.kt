package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.impl.utils.StandardTransferValidator
import com.marsofandrew.bookkeeper.transfers.impl.utils.toMoneyIsTransferredEvent
import org.apache.logging.log4j.LogManager

internal class StandardTransferCreator<T : CommonTransferBase>(
    private val transferStorage: TransferStorage,
    private val eventPublisher: EventPublisher,
    transferAccountValidator: TransferAccountValidator,
    transferCategoryValidator: TransferCategoryValidator,
    private val mapper: (CommonTransferBase) -> T
) {

    private val logger = LogManager.getLogger()
    private val validator = StandardTransferValidator(transferAccountValidator, transferCategoryValidator)

    fun add(commonTransfer: T): T {
        validator.validate(commonTransfer)

        val createdTransfer = transferStorage.create(commonTransfer)
        logger.info("CommonTransfer with id ${createdTransfer.id} was created")

        eventPublisher.publish(createdTransfer.toMoneyIsTransferredEvent())
        return mapper(createdTransfer)
    }
}
