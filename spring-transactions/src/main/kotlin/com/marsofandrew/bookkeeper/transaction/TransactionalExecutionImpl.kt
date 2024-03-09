package com.marsofandrew.bookkeeper.transaction

import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
internal class TransactionalExecutionImpl(
    private val transactionalTemplate: TransactionTemplate
) : TransactionalExecution {

    // TODO: refactor it
    override fun <T> execute(block: () -> T): T = transactionalTemplate.execute {
        block()
    }!!
}