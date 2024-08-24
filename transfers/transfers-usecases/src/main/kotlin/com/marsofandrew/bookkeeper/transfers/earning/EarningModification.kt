package com.marsofandrew.bookkeeper.transfers.earning

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.EarningUpdate
import com.marsofandrew.bookkeeper.transfers.user.User

interface EarningModification {

    fun modify(userId: NumericId<User>, earning: EarningUpdate): Earning
}