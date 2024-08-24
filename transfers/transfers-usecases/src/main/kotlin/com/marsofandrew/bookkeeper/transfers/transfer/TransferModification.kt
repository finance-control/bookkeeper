package com.marsofandrew.bookkeeper.transfers.transfer

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.TransferUpdate
import com.marsofandrew.bookkeeper.transfers.user.User

interface TransferModification {

    fun modify(userId: NumericId<User>, earning: TransferUpdate): Transfer
}