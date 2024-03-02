package com.marsofandrew.bookkeeper.base.transaction

interface TransactionalExecution {

    fun <T> execute(block: () -> T): T
}