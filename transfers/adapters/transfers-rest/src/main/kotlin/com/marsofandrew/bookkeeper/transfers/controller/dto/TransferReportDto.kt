package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.transfers.TransferReport

data class TransferReportDto(
    val total: List<MoneyDto>
)

fun TransferReport.toTransferReportDto() = TransferReportDto(
    total = total.map { it.toMoneyDto() }
)
