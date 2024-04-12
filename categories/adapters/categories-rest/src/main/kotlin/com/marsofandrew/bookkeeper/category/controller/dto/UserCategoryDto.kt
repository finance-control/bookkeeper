package com.marsofandrew.bookkeeper.category.controller.dto

import com.marsofandrew.bookkeeper.category.UserCategory

internal data class UserCategoryDto(
    val id: Long,
    val userId: Long,
    val title: String,
    val description: String?
)

internal fun UserCategory.toUserCategoryDto() = UserCategoryDto(
    id = id.value,
    userId = userId.value,
    title = title,
    description = description
)
