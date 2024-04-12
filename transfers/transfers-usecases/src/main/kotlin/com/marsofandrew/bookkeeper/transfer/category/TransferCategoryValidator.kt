package com.marsofandrew.bookkeeper.transfer.category

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfer.user.User

interface TransferCategoryValidator {

    fun validate(userId: NumericId<User>, categoryId: NumericId<Category>): Boolean
}
