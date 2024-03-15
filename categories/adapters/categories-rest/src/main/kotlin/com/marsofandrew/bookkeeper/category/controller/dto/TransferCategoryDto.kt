package com.marsofandrew.bookkeeper.category.controller.dto

import com.marsofandrew.bookkeeper.category.TransferUserCategory

internal data class TransferCategoryDto(
    val id: Long,
    val userId: Long,
    val title: String,
    val description: String?
)

internal fun TransferUserCategory.toTransferCategoryDto() = TransferCategoryDto(
    id = id.value,
    userId = userId.value,
    title = title,
    description = description
)
