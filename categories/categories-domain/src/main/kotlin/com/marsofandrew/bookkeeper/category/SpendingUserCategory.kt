package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.base.exception.validateFiled
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

data class SpendingUserCategory(
    override val id: NumericId<SpendingUserCategory>,
    override val userId: NumericId<User>,
    override val title: String,
    override val description: String?,
    override val version: Version
) : UserCategory<SpendingUserCategory> {

    init {
        validateFiled(title.isNotBlank()) { "title is blank" }
    }
}
