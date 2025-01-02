package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.CommonTransferUpdateBase
import com.marsofandrew.bookkeeper.transfers.TransferWithCategory
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.CategorySelector
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.impl.utils.StandardTransferValidator
import com.marsofandrew.bookkeeper.transfers.impl.utils.toMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.transfers.impl.utils.toRollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.transfers.user.User
import org.apache.logging.log4j.LogManager

internal class StandardTransferModifier<T: CommonTransferBase, U: CommonTransferUpdateBase>(
    private val transferStorage: TransferStorage,
    private val eventPublisher: EventPublisher,
    private val categorySelector: CategorySelector,
    transferAccountValidator: TransferAccountValidator,
    transferCategoryValidator: TransferCategoryValidator,
    private val transactionExecutor: TransactionExecutor,
    private val mapper: (CommonTransferBase) -> T,
    private val updateBy: CommonTransferBase.(U) -> CommonTransferBase,
) {

    private val logger = LogManager.getLogger()
    private val validator = StandardTransferValidator(transferAccountValidator, transferCategoryValidator)

    fun modify(userId: NumericId<User>, modification: U): TransferWithCategory<T> = transactionExecutor.execute {
        val original = transferStorage.findByIdAndUserIdOrThrow(modification.id, userId)
        val updated = original.updateBy(modification)
        validator.validate(updated)

        transferStorage.update(updated)

        logger.info("CommonTransfer ${original.id} has been updated")
        eventPublisher.publish(original.toRollbackMoneyIsTransferredEvent())
        eventPublisher.publish(updated.toMoneyIsTransferredEvent())
        val updatedTransfer = mapper(updated)
        val category = categorySelector.select(userId, updatedTransfer.categoryId)

        TransferWithCategory(
            transfer = updatedTransfer,
            category = category,
        )
    }
}