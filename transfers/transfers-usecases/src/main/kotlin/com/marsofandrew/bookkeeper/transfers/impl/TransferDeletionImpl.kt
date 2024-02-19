package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.TransferDeletion

class TransferDeletionImpl(
    private val transferStorage: TransferStorage
) : TransferDeletion {

    override fun delete(ids: Collection<StringId<Transfer>>) {
        transferStorage.delete(ids)
    }
}