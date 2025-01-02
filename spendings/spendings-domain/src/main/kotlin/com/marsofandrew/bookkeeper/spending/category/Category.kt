package com.marsofandrew.bookkeeper.spending.category

import com.marsofandrew.bookkeeper.properties.id.NumericId


data class Category(
    val id: NumericId<Category>,
    val title: String,
    val description: String?,
)
