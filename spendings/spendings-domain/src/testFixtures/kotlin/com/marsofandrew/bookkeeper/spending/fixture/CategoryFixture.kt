package com.marsofandrew.bookkeeper.spending.fixture

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.category.Category

data class CategoryFixture(
    val id: NumericId<Category>
) {
    var title = "Test Category title"
    var description: String? = null

    fun build() = Category(
        id = id,
        title = title,
        description = description
    )
}