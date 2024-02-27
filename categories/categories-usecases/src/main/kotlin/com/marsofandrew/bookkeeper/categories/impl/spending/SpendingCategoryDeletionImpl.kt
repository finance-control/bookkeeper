package com.marsofandrew.bookkeeper.categories.impl.spending

import com.marsofandrew.bookkeeper.categories.CategoryDeletion
import com.marsofandrew.bookkeeper.categories.SpendingUserCategory
import com.marsofandrew.bookkeeper.categories.access.CategoryStorage
import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class SpendingCategoryDeletionImpl(
    private val spendingCategoryStorage: CategoryStorage<SpendingUserCategory>
) : CategoryDeletion<SpendingUserCategory> {
    override fun delete(userId: NumericId<User>, ids: Set<NumericId<SpendingUserCategory>>) {
        spendingCategoryStorage.delete(userId, ids)
    }
}