package com.marsofandrew.bookkeeper.category.impl

import com.marsofandrew.bookkeeper.category.CategoryAdding
import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import org.apache.logging.log4j.LogManager

class CategoryAddingImpl(
    private val categoryStorage: CategoryStorage
) : CategoryAdding {

    private val logger = LogManager.getLogger()

    override fun add(category: UserCategory): UserCategory {
        return categoryStorage.create(category)
            .also { logger.info("UserCategory $category was added") }
    }
}