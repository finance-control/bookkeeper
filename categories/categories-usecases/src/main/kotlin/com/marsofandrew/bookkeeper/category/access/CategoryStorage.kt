package com.marsofandrew.bookkeeper.category.access

import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategoryStorage {
    fun findAllByUserId(userId: NumericId<User>): List<UserCategory>
    fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Set<NumericId<UserCategory>>): List<UserCategory>

    fun existsByUserIdAndCategoryId(userId: NumericId<User>, id: NumericId<UserCategory>): Boolean

    fun create(userCategory: UserCategory): UserCategory
    fun delete(ids: Set<NumericId<UserCategory>>)
}