package com.marsofandrew.bookkeeper.categories

import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategorySelection<T : UserCategory<T>> {

    fun select(userId: NumericId<User>, ids: Set<NumericId<T>>): List<T>
    fun select(userId: NumericId<User>): List<T>
}