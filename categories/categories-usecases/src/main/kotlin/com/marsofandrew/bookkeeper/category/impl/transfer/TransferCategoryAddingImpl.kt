package com.marsofandrew.bookkeeper.category.impl.transfer

import com.marsofandrew.bookkeeper.category.CategoryAdding
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import org.apache.logging.log4j.LogManager

class TransferCategoryAddingImpl(
    private val transferUserCategoryStorage: CategoryStorage<TransferUserCategory>
) : CategoryAdding<TransferUserCategory> {

    private val logger = LogManager.getLogger()

    override fun add(category: TransferUserCategory): TransferUserCategory {
        return transferUserCategoryStorage.create(category)
            .also { logger.info("TransferUserCategory $category was added") }
    }
}
