package com.marsofandrew.bookkeeper.category.impl.spending

import com.marsofandrew.bookkeeper.category.CategoryAdding
import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import org.apache.logging.log4j.LogManager

class SpendingCategoryAddingImpl(
    private val spendingCategoryStorage: CategoryStorage<SpendingUserCategory>
) : CategoryAdding<SpendingUserCategory> {

    private val logger = LogManager.getLogger()

    override fun add(category: SpendingUserCategory): SpendingUserCategory {
        return spendingCategoryStorage.create(category)
            .also { logger.info("SpendingUserCategory $category was added") }
    }
}
