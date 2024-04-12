package com.marsofandrew.bookkeeper.category.impl

import com.marsofandrew.bookkeeper.category.CategoryValidation
import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class CategoryValidationImpl(
    private val categoryStorage: CategoryStorage
) : CategoryValidation {

    override fun validate(userId: NumericId<User>, categoryId: NumericId<UserCategory>): Boolean {
        return categoryStorage.existsByUserIdAndCategoryId(userId, categoryId)
    }
}