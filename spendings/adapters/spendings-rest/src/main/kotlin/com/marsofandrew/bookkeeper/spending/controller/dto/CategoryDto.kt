package com.marsofandrew.bookkeeper.spending.controller.dto

import com.marsofandrew.bookkeeper.spending.category.Category

internal data class CategoryDto(
    val id: Long,
    val title: String,
)

internal fun Category.toCategoryDto() = CategoryDto(
    id = id.value,
    title = title,
)
