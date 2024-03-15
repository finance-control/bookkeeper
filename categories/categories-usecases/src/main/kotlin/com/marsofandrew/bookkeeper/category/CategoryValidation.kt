package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategoryValidation<T : UserCategory<T>> {

    fun validate(userId: NumericId<User>, categoryId: NumericId<T>): Boolean
}