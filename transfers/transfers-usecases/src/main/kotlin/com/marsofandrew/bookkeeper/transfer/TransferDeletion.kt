package com.marsofandrew.bookkeeper.transfer

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfer.user.User

interface TransferDeletion {

    fun delete(userId: NumericId<User>, ids: Collection<NumericId<Transfer>>)
}