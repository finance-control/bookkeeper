package com.marsofandrew.bookkeeper.spending.category

import com.marsofandrew.bookkeeper.category.CategoryValidation
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.user.User
import org.springframework.stereotype.Service

@Service
internal class SpendingCategoryValidatorImpl(
    private val categoryValidation: CategoryValidation
) : SpendingCategoryValidator {

    override fun validate(userId: NumericId<User>, categoryId: NumericId<Category>): Boolean {
        return categoryValidation.validate(userId.value.asId(), categoryId.value.asId())
    }
}