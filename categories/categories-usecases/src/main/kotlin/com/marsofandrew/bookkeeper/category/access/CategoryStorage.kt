package com.marsofandrew.bookkeeper.category.access

import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategoryStorage<T : UserCategory<T>> {
    fun findAllByUserId(userId: NumericId<User>): List<T>
    fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Set<NumericId<T>>): List<T>

    fun existsByUserIdAndCategoryId(userId: NumericId<User>, id: NumericId<T>): Boolean

    fun create(userCategory: T): T
    fun delete(ids: Set<NumericId<T>>)
}