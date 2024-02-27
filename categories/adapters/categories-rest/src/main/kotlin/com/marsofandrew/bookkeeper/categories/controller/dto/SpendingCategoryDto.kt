package com.marsofandrew.bookkeeper.categories.controller.dto

import com.marsofandrew.bookkeeper.categories.SpendingUserCategory

internal data class SpendingCategoryDto(
    val id: Long,
    val userId: Long,
    val title: String,
    val description: String?
)

internal fun SpendingUserCategory.toSpendingCategoryDto() = SpendingCategoryDto(
    id = id.value,
    userId = userId.value,
    title = title,
    description = description
)