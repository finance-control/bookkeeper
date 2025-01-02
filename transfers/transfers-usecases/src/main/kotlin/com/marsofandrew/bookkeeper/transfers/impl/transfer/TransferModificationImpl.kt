package com.marsofandrew.bookkeeper.transfers.impl.transfer

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.TransferUpdate
import com.marsofandrew.bookkeeper.transfers.TransferWithCategory
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.CategorySelector
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransferModifier
import com.marsofandrew.bookkeeper.transfers.transfer.TransferModification
import com.marsofandrew.bookkeeper.transfers.updateTransfer
import com.marsofandrew.bookkeeper.transfers.user.User

class TransferModificationImpl(
    transferStorage: TransferStorage,
    eventPublisher: EventPublisher,
    categorySelector: CategorySelector,
    transferAccountValidator: TransferAccountValidator,
    transferCategoryValidator: TransferCategoryValidator,
    transactionExecutor: TransactionExecutor
) : TransferModification {

    private val modifier = StandardTransferModifier(
        transferStorage,
        eventPublisher,
        categorySelector,
        transferAccountValidator,
        transferCategoryValidator,
        transactionExecutor,
        Transfer::of,
        CommonTransferBase::updateTransfer
    )

    override fun modify(userId: NumericId<User>, earning: TransferUpdate): TransferWithCategory<Transfer> {
        return modifier.modify(userId, earning)
    }
}