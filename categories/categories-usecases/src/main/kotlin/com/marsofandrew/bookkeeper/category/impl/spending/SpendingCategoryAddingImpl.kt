package com.marsofandrew.bookkeeper.category.impl.spending

import com.marsofandrew.bookkeeper.category.CategoryAdding
import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage

class SpendingCategoryAddingImpl(
    private val spendingCategoryStorage: CategoryStorage<SpendingUserCategory>
) : CategoryAdding<SpendingUserCategory> {

    override fun add(category: SpendingUserCategory): SpendingUserCategory {
        return spendingCategoryStorage.create(category)
    }
}
