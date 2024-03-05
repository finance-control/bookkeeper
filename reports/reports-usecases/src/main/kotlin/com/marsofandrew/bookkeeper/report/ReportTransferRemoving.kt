package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.report.transfer.Transfer

interface ReportTransferRemoving {

    fun remove(transfer: Transfer)
}