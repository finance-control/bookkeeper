package com.marsofandrew.bookkeeper.categories

import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

data class TransferUserCategory(
    override val id: NumericId<TransferUserCategory>,
    override val userId: NumericId<User>,
    override val title: String,
    override val description: String?
): UserCategory<TransferUserCategory>
