package com.marsofandrew.bookkeeper.transfers.impl.earning

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.TransferWithCategory
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.TransferCategorySelector
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.earning.EarningAdding
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransferCreator

class EarningAddingImpl(
    transferStorage: TransferStorage,
    eventPublisher: EventPublisher,
    transferCategorySelector: TransferCategorySelector,
    transferAccountValidator: TransferAccountValidator,
    transferCategoryValidator: TransferCategoryValidator
) : EarningAdding {

    private val standardTransferCreator = StandardTransferCreator(
        transferStorage,
        eventPublisher,
        transferCategorySelector,
        transferAccountValidator,
        transferCategoryValidator,
    ) {
        Earning.of(it)
    }

    override fun add(transfer: Earning): TransferWithCategory<Earning> = standardTransferCreator.add(transfer)
}

