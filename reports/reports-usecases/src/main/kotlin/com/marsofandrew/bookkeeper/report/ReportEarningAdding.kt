package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.report.earning.Earning
import com.marsofandrew.bookkeeper.report.transfer.Transfer

interface ReportEarningAdding {

    fun add(earning: Earning)
}