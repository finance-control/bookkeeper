package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.transfers.category.TransferCategory
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

data class Transfer(
    val id: StringId<Transfer>,
    val userId: NumericId<User>,
    val date: LocalDate,
    val send: PositiveMoney?,
    val received: PositiveMoney,
    val comment: String,
    val transferCategoryId: NumericId<TransferCategory>,
    val fee: PositiveMoney? = null
)

