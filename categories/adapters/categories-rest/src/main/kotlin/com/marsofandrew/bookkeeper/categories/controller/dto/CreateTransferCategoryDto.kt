package com.marsofandrew.bookkeeper.categories.controller.dto

import com.marsofandrew.bookkeeper.categories.TransferUserCategory
import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

internal data class CreateTransferCategoryDto(
    val title: String,
    val description: String?
) {

    fun toTransferUserCategory(userId: NumericId<User>) = TransferUserCategory(
        id = NumericId.unidentified(),
        userId = userId,
        title = title,
        description = description
    )
}