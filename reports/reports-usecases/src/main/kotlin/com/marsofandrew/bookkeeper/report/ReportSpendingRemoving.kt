package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.report.spending.Spending

interface ReportSpendingRemoving {

    fun remove(spending: Spending)
}