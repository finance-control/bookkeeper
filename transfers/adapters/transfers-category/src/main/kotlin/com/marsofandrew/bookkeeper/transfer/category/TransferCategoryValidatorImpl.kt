package com.marsofandrew.bookkeeper.transfer.category

import com.marsofandrew.bookkeeper.category.CategoryValidation
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.user.User
import org.springframework.stereotype.Service

@Service
internal class TransferCategoryValidatorImpl(
    private val categoryValidation: CategoryValidation<TransferUserCategory>
) : TransferCategoryValidator {

    override fun validate(userId: NumericId<User>, categoryId: NumericId<TransferCategory>): Boolean {
        return categoryValidation.validate(userId.value.asId(), categoryId.value.asId())
    }
}