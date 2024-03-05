package com.marsofandrew.bookkeeper.account

interface AccountCleanup {

    fun clean(batchSize: Int)
}