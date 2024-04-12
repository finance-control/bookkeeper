package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategoryValidation {

    fun validate(userId: NumericId<User>, categoryId: NumericId<UserCategory>): Boolean
}