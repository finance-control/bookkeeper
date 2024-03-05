package com.marsofandrew.bookkeeper.transfer.impl

import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.TransferAdding
import com.marsofandrew.bookkeeper.transfer.impl.utils.toAccountBoundedMoney

class TransferAddingImpl(
    private val transferStorage: TransferStorage,
    private val eventPublisher: EventPublisher,
) : TransferAdding {

    override fun add(transfer: Transfer): Transfer {
        val createdTransfer = transferStorage.create(transfer)
        eventPublisher.publish(createdTransfer.toMoneyIsTransferredEvent())
        return createdTransfer
    }
}

private fun Transfer.toMoneyIsTransferredEvent() = MoneyIsTransferredEvent(
    userId = userId.value,
    date = date,
    send = send?.toAccountBoundedMoney(),
    received = received.toAccountBoundedMoney(),
    category = transferCategoryId.value
)

