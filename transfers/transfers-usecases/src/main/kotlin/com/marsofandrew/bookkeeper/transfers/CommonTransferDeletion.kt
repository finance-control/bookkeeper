package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.user.User

interface CommonTransferDeletion {

    fun delete(userId: NumericId<User>, ids: Collection<NumericId<CommonTransfer>>)
}