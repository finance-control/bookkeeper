package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.transfers.category.Category


internal data class CategoryDto(
    val id: Long,
    val title: String,
)

internal fun Category.toCategoryDto() = CategoryDto(
    id = id.value,
    title = title,
)
