package com.marsofandrew.bookkeeper.transfers.impl.earning

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.earning.EarningAdding
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransferCreator

class EarningAddingImpl(
    transferStorage: TransferStorage,
    eventPublisher: EventPublisher,
    transferAccountValidator: TransferAccountValidator,
    transferCategoryValidator: TransferCategoryValidator
) : EarningAdding {

    private val standardTransferCreator = StandardTransferCreator(
        transferStorage,
        eventPublisher,
        transferAccountValidator,
        transferCategoryValidator,
    ) {
        Earning.of(it)
    }

    override fun add(transfer: Earning): Earning = standardTransferCreator.add(transfer)
}

