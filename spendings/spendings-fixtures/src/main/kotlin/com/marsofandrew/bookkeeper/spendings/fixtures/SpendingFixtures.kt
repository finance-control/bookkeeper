package com.marsofandrew.bookkeeper.spendings.fixtures

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.user.User

fun spending(
    id: NumericId<Spending>,
    userId: NumericId<User>,
    init: SpendingFixture.() -> Unit = {}
): Spending = SpendingFixture(id, userId)
    .apply(init)
    .build()