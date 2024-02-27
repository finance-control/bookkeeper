package com.marsofandrew.bookkeeper.categories.impl.spending

import com.marsofandrew.bookkeeper.categories.CategoryAdding
import com.marsofandrew.bookkeeper.categories.SpendingUserCategory
import com.marsofandrew.bookkeeper.categories.access.CategoryStorage

class SpendingCategoryAddingImpl(
    private val spendingCategoryStorage: CategoryStorage<SpendingUserCategory>
) : CategoryAdding<SpendingUserCategory> {
    override fun add(category: SpendingUserCategory): SpendingUserCategory {
        return spendingCategoryStorage.create(category)
    }
}