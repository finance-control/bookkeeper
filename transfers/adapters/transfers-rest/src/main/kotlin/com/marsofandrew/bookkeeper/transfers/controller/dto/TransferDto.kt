package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.TransferWithCategory
import com.marsofandrew.bookkeeper.transfers.category.Category
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
internal data class TransferDto(
    val id: Long,
    val userId: Long,
    val date: String,
    val send: AccountMoneyDto?,
    val received: AccountMoneyDto,
    val description: String,
    @Schema(deprecated = true)
    @Deprecated(message = "Moved to category.id", replaceWith = ReplaceWith("category.id"))
    val categoryId: Long,
    val category: CategoryDto
)

internal fun <T : CommonTransferBase> TransferWithCategory<T>.toTransferDto() = TransferDto(
    id = transfer.id.value,
    userId = transfer.userId.value,
    date = transfer.date.toString(),
    send = transfer.send?.toAccountsMoneyDto(),
    received = transfer.received.toAccountsMoneyDto(),
    description = transfer.description,
    categoryId = transfer.categoryId.value,
    category = category.toCategoryDto()
)