package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategoryDeletion {

    fun delete(userId: NumericId<User>, ids: Set<NumericId<UserCategory>>)
}