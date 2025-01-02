package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.transfers.TransferReport

internal data class TransferReportDto(
    val total: List<MoneyDto>
    // TODO: add by category
)

internal fun TransferReport.toTransferReportDto() = TransferReportDto(
    total = total.map { it.toMoneyDto() }
)
