package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.TransferAdding

class TransferAddingImpl(
    private val transferStorage: TransferStorage
) : TransferAdding {

    override fun add(transfer: Transfer): Transfer {
        return transferStorage.create(transfer)
    }
}