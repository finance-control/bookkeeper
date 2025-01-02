package com.marsofandrew.bookkeeper.transfers.earning

import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.TransferWithCategory

interface EarningAdding {

    fun add(transfer: Earning): TransferWithCategory<Earning>
}