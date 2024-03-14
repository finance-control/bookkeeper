package com.marsofandrew.bookkeeper.base.transaction

class TestTransactionExecutor : TransactionExecutor {

    override fun <T> execute(block: () -> T): T {
        return block()
    }
}