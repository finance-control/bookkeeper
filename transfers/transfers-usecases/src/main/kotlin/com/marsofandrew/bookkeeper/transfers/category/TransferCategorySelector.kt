package com.marsofandrew.bookkeeper.transfers.category

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.user.User

interface TransferCategorySelector {

    // has to be safe
    fun select(userId: NumericId<User>, categoryId: NumericId<Category>): Category

    fun selectAllByIds(userId: NumericId<User>, ids: List<NumericId<Category>>): List<Category>
}
