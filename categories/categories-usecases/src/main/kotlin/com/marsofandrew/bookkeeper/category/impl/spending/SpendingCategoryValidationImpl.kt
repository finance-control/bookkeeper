package com.marsofandrew.bookkeeper.category.impl.spending

import com.marsofandrew.bookkeeper.category.CategoryValidation
import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class SpendingCategoryValidationImpl(
    private val categoryStorage: CategoryStorage<SpendingUserCategory>
) : CategoryValidation<SpendingUserCategory> {

    override fun validate(userId: NumericId<User>, categoryId: NumericId<SpendingUserCategory>): Boolean {
       return categoryStorage.existsByUserIdAndCategoryId(userId, categoryId)
    }
}