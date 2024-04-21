package com.marsofandrew.bookkeeper.transfers.earning

import com.marsofandrew.bookkeeper.transfers.Earning

interface EarningAdding {

    fun add(transfer: Earning): Earning
}