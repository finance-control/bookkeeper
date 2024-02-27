package com.marsofandrew.bookkeeper.categories

import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategoryDeletion<T : UserCategory<T>> {

    fun delete(userId: NumericId<User>, ids: Set<NumericId<T>>)
}