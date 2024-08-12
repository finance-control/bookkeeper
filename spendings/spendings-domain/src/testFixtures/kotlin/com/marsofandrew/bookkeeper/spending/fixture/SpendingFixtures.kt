package com.marsofandrew.bookkeeper.spending.fixture

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingUpdate
import com.marsofandrew.bookkeeper.spending.user.User

fun spending(
    id: NumericId<Spending>,
    userId: NumericId<User>,
    init: SpendingFixture.() -> Unit = {}
): Spending = SpendingFixture(id, userId)
    .apply(init)
    .build()

fun spendingUpdate(
    id: NumericId<Spending>,
    init: SpendingUpdateFixture.() -> Unit = {}
): SpendingUpdate = SpendingUpdateFixture(id)
    .apply(init)
    .build()