package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.user.User

interface SpendingModification {

    fun modify(userId: NumericId<User>, modification: SpendingUpdate): Spending
}