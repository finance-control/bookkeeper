package com.marsofandrew.bookkeeper.spending.category

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.user.User

interface CategoryValidator {

    fun validate(userId: NumericId<User>, categoryId: NumericId<SpendingCategory>): Boolean
}