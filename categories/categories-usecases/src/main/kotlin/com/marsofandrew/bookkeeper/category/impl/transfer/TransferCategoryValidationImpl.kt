package com.marsofandrew.bookkeeper.category.impl.transfer

import com.marsofandrew.bookkeeper.category.CategoryValidation
import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class TransferCategoryValidationImpl(
    private val categoryStorage: CategoryStorage<TransferUserCategory>
) : CategoryValidation<TransferUserCategory> {

    override fun validate(userId: NumericId<User>, categoryId: NumericId<TransferUserCategory>): Boolean {
       return categoryStorage.existsByUserIdAndCategoryId(userId, categoryId)
    }
}