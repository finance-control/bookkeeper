package com.marsofandrew.bookkeeper.category.controller.dto

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

internal data class CreateSpendingCategoryDto(
    val title: String,
    val description: String?
) {

    fun toSpendingCategory(userId: NumericId<User>) = SpendingUserCategory(
        id = NumericId.unidentified(),
        userId = userId,
        title = title,
        description = description,
        version = Version(0)
    )
}
