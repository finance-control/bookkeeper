package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.transfers.TransferReport

internal data class TransferReportDto(
    val total: List<MoneyDto>,
    val byCategory: Map<Long, List<MoneyDto>>
)

internal fun TransferReport.toTransferReportDto() = TransferReportDto(
    total = total.map { it.toMoneyDto() },
    byCategory = byCategory.map { (categoryId, moneys) -> categoryId.value to moneys.map { it.toMoneyDto() } }.toMap()
)
