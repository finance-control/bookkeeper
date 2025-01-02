package com.marsofandrew.bookkeeper.transfers.impl.transfer

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.TransferWithCategory
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.CategorySelector
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransferCreator
import com.marsofandrew.bookkeeper.transfers.transfer.TransferAdding

class TransferAddingImpl(
    transferStorage: TransferStorage,
    eventPublisher: EventPublisher,
    categorySelector: CategorySelector,
    transferAccountValidator: TransferAccountValidator,
    transferCategoryValidator: TransferCategoryValidator
) : TransferAdding {

    private val standardTransferCreator = StandardTransferCreator(
        transferStorage,
        eventPublisher,
        categorySelector,
        transferAccountValidator,
        transferCategoryValidator,
    ) {
        Transfer.of(it)
    }

    override fun add(transfer: Transfer): TransferWithCategory<Transfer> = standardTransferCreator.add(transfer)
}

