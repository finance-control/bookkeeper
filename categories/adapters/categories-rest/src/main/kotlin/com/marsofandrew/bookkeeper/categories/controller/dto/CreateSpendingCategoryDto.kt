package com.marsofandrew.bookkeeper.categories.controller.dto

import com.marsofandrew.bookkeeper.categories.SpendingUserCategory
import com.marsofandrew.bookkeeper.categories.TransferUserCategory
import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

internal data class CreateSpendingCategoryDto(
    val title: String,
    val description: String?
) {

    fun toSpendingCategory(userId: NumericId<User>) = SpendingUserCategory(
        id = NumericId.unidentified(),
        userId = userId,
        title = title,
        description = description
    )
}