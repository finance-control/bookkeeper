package com.marsofandrew.bookkeeper.category.impl

import com.marsofandrew.bookkeeper.category.CategorySelection
import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class CategorySelectionImpl(
    private val categoryStorage: CategoryStorage
) : CategorySelection {

    override fun select(
        userId: NumericId<User>,
        ids: Set<NumericId<UserCategory>>
    ): List<UserCategory> {
        return categoryStorage.findAllByUserIdAndIds(userId, ids)
    }

    override fun select(userId: NumericId<User>): List<UserCategory> {
        return categoryStorage.findAllByUserId(userId)
    }
}
