package com.marsofandrew.bookkeeper.transfers

interface TransferAdding {

    fun add(transfer: Transfer): Transfer
}