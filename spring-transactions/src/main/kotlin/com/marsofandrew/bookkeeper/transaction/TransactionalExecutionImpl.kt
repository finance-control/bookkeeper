package com.marsofandrew.bookkeeper.transaction

import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import org.springframework.stereotype.Service

@Service
internal class TransactionalExecutionImpl(
) : TransactionalExecution {
    override fun <T> execute(block: () -> T): T {
       return block()
    }
}