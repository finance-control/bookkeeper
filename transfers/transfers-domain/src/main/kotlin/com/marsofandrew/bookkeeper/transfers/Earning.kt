package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.category.Category
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

data class Earning(
    override val id: NumericId<CommonTransfer>,
    override val userId: NumericId<User>,
    override val date: LocalDate,
    override val received: AccountMoney,
    override val description: String,
    override val categoryId: NumericId<Category>,
    override val createdAt: LocalDate,
    override val version: Version
) : CommonTransferBase {
    override val send: AccountMoney? = null

    init {
        validateFiled(createdAt >= LocalDate.of(2024, 1, 1)) { "Created at date is too early" }
        validateFiled(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }

    companion object {
        fun of(commonTransfer: CommonTransferBase): Earning {
            validateFiled(commonTransfer.send == null) { "This ${commonTransfer.id} is not an earning" }

            return Earning(
                id = commonTransfer.id,
                userId = commonTransfer.userId,
                date = commonTransfer.date,
                received = commonTransfer.received,
                description = commonTransfer.description,
                categoryId = commonTransfer.categoryId,
                createdAt = commonTransfer.createdAt,
                version = commonTransfer.version
            )
        }
    }
}