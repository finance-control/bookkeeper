package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.transfers.user.User

interface TransferDeletion {

    fun delete(userId: NumericId<User>, ids: Collection<NumericId<Transfer>>)
}