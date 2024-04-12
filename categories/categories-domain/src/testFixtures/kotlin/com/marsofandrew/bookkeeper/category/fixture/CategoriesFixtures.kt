package com.marsofandrew.bookkeeper.category.fixture

import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.properties.id.NumericId

fun userCategory(
    id: NumericId<UserCategory>,
    init: UserCategoryFixture.() -> Unit = {}
): UserCategory = UserCategoryFixture(id).apply(init).build()

