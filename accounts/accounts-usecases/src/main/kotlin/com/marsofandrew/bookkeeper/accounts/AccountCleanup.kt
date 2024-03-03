package com.marsofandrew.bookkeeper.accounts

interface AccountCleanup {

    fun clean(batchSize: Int)
}