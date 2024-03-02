package com.marsofandrew.bookkeeper.spendings

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spendings.user.User

interface SpendingDeletion {

    fun delete(userId: NumericId<User>, ids: Collection<NumericId<Spending>>) //TODO: check by userId
}