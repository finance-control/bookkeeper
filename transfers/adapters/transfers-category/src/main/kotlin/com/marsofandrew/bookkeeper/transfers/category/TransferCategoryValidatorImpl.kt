package com.marsofandrew.bookkeeper.transfers.category

import com.marsofandrew.bookkeeper.category.CategoryValidation
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.user.User
import org.springframework.stereotype.Service

@Service
internal class TransferCategoryValidatorImpl(
    private val categoryValidation: CategoryValidation
) : TransferCategoryValidator {

    override fun validate(userId: NumericId<User>, categoryId: NumericId<Category>): Boolean {
        return categoryValidation.validate(userId.value.asId(), categoryId.value.asId())
    }
}