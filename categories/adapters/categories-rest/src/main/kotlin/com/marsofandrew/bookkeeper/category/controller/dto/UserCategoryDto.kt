package com.marsofandrew.bookkeeper.category.controller.dto

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.properties.id.asId

internal data class UserCategoryDto(
    val id: Long,
    val userId: Long,
    val title: String,
    val description: String?,
    val version: Int
)

internal fun UserCategory.toUserCategoryDto() = UserCategoryDto(
    id = id.value,
    userId = userId.value,
    title = title,
    description = description,
    version = version.value
)

internal fun UserCategoryDto.toUserCategory() = UserCategory(
    id = id.asId(),
    userId = userId.asId(),
    title = title,
    description = description,
    version = Version(version)
)
