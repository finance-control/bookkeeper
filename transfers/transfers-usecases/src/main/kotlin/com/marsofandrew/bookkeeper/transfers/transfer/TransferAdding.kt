package com.marsofandrew.bookkeeper.transfers.transfer

import com.marsofandrew.bookkeeper.transfers.Transfer

interface TransferAdding {

    fun add(transfer: Transfer): Transfer
}