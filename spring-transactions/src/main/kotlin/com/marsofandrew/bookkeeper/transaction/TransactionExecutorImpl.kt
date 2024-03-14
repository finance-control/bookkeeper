package com.marsofandrew.bookkeeper.transaction

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
internal class TransactionExecutorImpl(
    private val transactionalTemplate: TransactionTemplate
) : TransactionExecutor {

    override fun <T> execute(block: () -> T): T = transactionalTemplate.execute {
        block()
    }!!
}
