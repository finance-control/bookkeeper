package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.base.exception.validateFiled
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

data class TransferUserCategory(
    override val id: NumericId<TransferUserCategory>,
    override val userId: NumericId<User>,
    override val title: String,
    override val description: String?,
    override val version: Version
) : UserCategory<TransferUserCategory> {

    init {
        validateFiled(title.isNotBlank()) { "title is blank" }
    }
}
