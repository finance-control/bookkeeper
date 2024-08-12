package com.marsofandrew.bookkeeper.category.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.category.CategoryModification
import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.exception.ValidationException
import com.marsofandrew.bookkeeper.properties.id.NumericId

class CategoryModificationImpl(
    private val categoryStorage: CategoryStorage,
    private val transactionExecutor: TransactionExecutor
) : CategoryModification {

    override fun modify(userId: NumericId<User>, category: UserCategory): UserCategory {
        validateCategory(userId, category)

        return transactionExecutor.execute {
            categoryStorage.findByUserIdAndIdOrThrow(userId, category.id)
            categoryStorage.update(category)
        }
    }

    private fun validateCategory(userId: NumericId<User>, category: UserCategory) {
        if (category.userId != userId) {
            throw ValidationException("User ID doesn't match category ID $category")
        }
    }
}
