package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.report.transfer.Transfer

interface ReportTransferAdding {

    fun add(transfer: Transfer)
}