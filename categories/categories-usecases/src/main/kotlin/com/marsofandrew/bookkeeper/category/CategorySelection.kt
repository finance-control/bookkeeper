package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategorySelection<T : UserCategory<T>> {

    fun select(userId: NumericId<User>, ids: Set<NumericId<T>>): List<T>
    fun select(userId: NumericId<User>): List<T>
}