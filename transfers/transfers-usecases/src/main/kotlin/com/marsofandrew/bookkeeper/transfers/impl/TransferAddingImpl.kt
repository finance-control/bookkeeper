package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.events.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.events.publisher.EventPublisher
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.TransferAdding
import com.marsofandrew.bookkeeper.transfers.impl.utils.toAccountBoundedMoney

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

