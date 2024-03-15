package com.marsofandrew.bookkeeper.category.impl.spending

import com.marsofandrew.bookkeeper.category.CategoryDeletion
import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class SpendingCategoryDeletionImpl(
    private val spendingCategoryStorage: CategoryStorage<SpendingUserCategory>
) : CategoryDeletion<SpendingUserCategory> {

    override fun delete(userId: NumericId<User>, ids: Set<NumericId<SpendingUserCategory>>) {
        spendingCategoryStorage.findAllByUserIdAndIds(userId, ids)
            .mapTo(HashSet()) { it.id }
            .let { spendingCategoryStorage.delete(it) }
    }
}
