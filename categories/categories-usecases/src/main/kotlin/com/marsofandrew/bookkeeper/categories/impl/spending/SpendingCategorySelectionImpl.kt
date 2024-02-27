package com.marsofandrew.bookkeeper.categories.impl.spending

import com.marsofandrew.bookkeeper.categories.CategorySelection
import com.marsofandrew.bookkeeper.categories.SpendingUserCategory
import com.marsofandrew.bookkeeper.categories.access.CategoryStorage
import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class SpendingCategorySelectionImpl(
    private val spendingCategoryStorage: CategoryStorage<SpendingUserCategory>
) : CategorySelection<SpendingUserCategory> {
    override fun select(
        userId: NumericId<User>,
        ids: Set<NumericId<SpendingUserCategory>>
    ): List<SpendingUserCategory> {
        return spendingCategoryStorage.findAllByUserIdAndIds(userId, ids)
    }

    override fun select(userId: NumericId<User>): List<SpendingUserCategory> {
        return spendingCategoryStorage.findAllByUserId(userId)
    }
}