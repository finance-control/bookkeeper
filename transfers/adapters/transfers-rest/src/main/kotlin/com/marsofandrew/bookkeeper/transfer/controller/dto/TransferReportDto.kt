package com.marsofandrew.bookkeeper.transfer.controller.dto

import com.marsofandrew.bookkeeper.transfer.TransferReport

internal data class TransferReportDto(
    val total: List<MoneyDto>
)

internal fun TransferReport.toTransferReportDto() = TransferReportDto(
    total = total.map { it.toMoneyDto() }
)
