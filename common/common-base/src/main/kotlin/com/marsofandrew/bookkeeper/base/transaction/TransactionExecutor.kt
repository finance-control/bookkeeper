package com.marsofandrew.bookkeeper.base.transaction

interface TransactionExecutor {

    fun <T> execute(block: () -> T): T
}
