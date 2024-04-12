package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId

data class UserCategory(
    override val id: NumericId<UserCategory>,
    val userId: NumericId<User>,
    val title: String,
    val description: String?,
    override val version: Version
): DomainModel {
    init {
        validateFiled(title.isNotBlank()) { "title is blank" }
    }
}
