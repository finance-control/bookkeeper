package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategoryDeletion<T : UserCategory<T>> {

    fun delete(userId: NumericId<User>, ids: Set<NumericId<T>>)
}