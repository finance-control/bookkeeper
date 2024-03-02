package com.marsofandrew.bookkeeper.categories.access

import com.marsofandrew.bookkeeper.categories.UserCategory
import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategoryStorage<T : UserCategory<T>> {
    fun findAllByUserId(userId: NumericId<User>): List<T>
    fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Set<NumericId<T>>): List<T>

    fun create(userCategory: T): T
    fun delete(ids: Set<NumericId<T>>)
}