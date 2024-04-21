package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.category.Category
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

data class CommonTransfer(
    override val id: NumericId<CommonTransfer>,
    override val userId: NumericId<User>,
    override val date: LocalDate,
    override val send: AccountMoney?,
    override val received: AccountMoney,
    override val description: String,
    override val categoryId: NumericId<Category>,
    override val createdAt: LocalDate,
    override val version: Version,
): CommonTransferBase {
    init {
        validateFiled(createdAt >= LocalDate.of(2024, 1, 1)) { "Created at date is too early" }
        validateFiled(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }

    companion object {

        fun of(commonTransferBase: CommonTransferBase) = CommonTransfer(
            id = commonTransferBase.id,
            userId = commonTransferBase.userId,
            date = commonTransferBase.date,
            send = commonTransferBase.send,
            received = commonTransferBase.received,
            description = commonTransferBase.description,
            categoryId = commonTransferBase.categoryId,
            createdAt = commonTransferBase.createdAt,
            version = commonTransferBase.version
        )
    }
}
