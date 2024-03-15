package com.marsofandrew.bookkeeper.category.impl.spending

import com.marsofandrew.bookkeeper.category.CategorySelection
import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
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
