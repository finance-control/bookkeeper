package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.category.Category
import com.marsofandrew.bookkeeper.spending.category.CategorySelector
import com.marsofandrew.bookkeeper.spending.fixture.category
import com.marsofandrew.bookkeeper.spending.user.User

internal class TestCategorySelector : CategorySelector {
    override fun select(
        userId: NumericId<User>,
        categoryId: NumericId<Category>
    ): Category {
        return category(id = categoryId)
    }

    override fun selectAllByIds(
        userId: NumericId<User>,
        ids: List<NumericId<Category>>
    ): List<Category> {
        return ids.map { id -> category(id = id) }
    }
}