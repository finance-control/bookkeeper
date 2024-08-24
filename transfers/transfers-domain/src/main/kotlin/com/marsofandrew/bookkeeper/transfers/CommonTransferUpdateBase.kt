package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.category.Category
import java.time.LocalDate

interface CommonTransferUpdateBase {
    val id: NumericId<CommonTransfer>
    val date: LocalDate?
    val send: AccountMoney?
    val received: AccountMoney?
    val description: String?
    val categoryId: NumericId<Category>?
    val version: Version
}