package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.report.earning.Earning

interface ReportEarningRemoving {

    fun remove(earning: Earning)
}