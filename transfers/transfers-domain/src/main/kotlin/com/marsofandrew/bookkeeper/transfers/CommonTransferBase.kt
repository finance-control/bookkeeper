package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.category.Category
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

interface CommonTransferBase: DomainModel {
    override val id: NumericId<CommonTransfer>
    val userId: NumericId<User>
    val date: LocalDate
    val send: AccountMoney?
    val received: AccountMoney
    val description: String
    val categoryId: NumericId<Category>
    val createdAt: LocalDate
    override val version: Version
}