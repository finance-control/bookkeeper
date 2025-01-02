package com.marsofandrew.bookkeeper.transfers.fixtures

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.category.Category

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
