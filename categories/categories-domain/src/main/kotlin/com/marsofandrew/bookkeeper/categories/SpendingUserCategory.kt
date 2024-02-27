package com.marsofandrew.bookkeeper.categories

import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

data class SpendingUserCategory(
    override val id: NumericId<SpendingUserCategory>,
    override val userId: NumericId<User>,
    override val title: String,
    override val description: String?
): UserCategory<SpendingUserCategory>
