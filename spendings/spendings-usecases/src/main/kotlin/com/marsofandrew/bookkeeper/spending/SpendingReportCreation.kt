package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.category.SpendingCategory
import com.marsofandrew.bookkeeper.spending.user.User
import java.time.LocalDate

interface SpendingReportCreation {

    fun createReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate,
        categories: Set<NumericId<SpendingCategory>>? = null
    ): SpendingReport
}