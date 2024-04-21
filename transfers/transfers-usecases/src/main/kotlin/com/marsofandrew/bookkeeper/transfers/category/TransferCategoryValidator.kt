package com.marsofandrew.bookkeeper.transfers.category

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.user.User

interface TransferCategoryValidator {

    fun validate(userId: NumericId<User>, categoryId: NumericId<Category>): Boolean
}
