package com.marsofandrew.bookkeeper.spending.category

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.user.User

interface CategorySelector {

    // has to be safe
    fun select(userId: NumericId<User>, categoryId: NumericId<Category>) : Category

    fun selectAllByIds(userId: NumericId<User>, ids: List<NumericId<Category>>) : List<Category>
}