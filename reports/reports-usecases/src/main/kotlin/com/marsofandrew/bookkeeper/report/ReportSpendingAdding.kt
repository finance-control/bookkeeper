package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.report.spending.Spending

interface ReportSpendingAdding {

    fun add(spending: Spending)
}