package com.marsofandrew.bookkeeper.transfers.transfer

import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.TransferWithCategory

interface TransferAdding {

    fun add(transfer: Transfer): TransferWithCategory<Transfer>
}