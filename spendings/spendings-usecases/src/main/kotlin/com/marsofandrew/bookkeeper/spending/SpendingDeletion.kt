package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.user.User

interface SpendingDeletion {

    fun delete(userId: NumericId<User>, ids: Collection<NumericId<Spending>>)
}
