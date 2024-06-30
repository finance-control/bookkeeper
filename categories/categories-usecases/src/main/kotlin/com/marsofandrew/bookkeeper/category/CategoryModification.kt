package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategoryModification {

    fun modify(userId: NumericId<User>, category: UserCategory): UserCategory
}