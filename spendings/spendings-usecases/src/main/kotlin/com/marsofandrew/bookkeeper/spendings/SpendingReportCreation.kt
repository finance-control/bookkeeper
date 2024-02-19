package com.marsofandrew.bookkeeper.spendings

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spendings.category.SpendingCategory
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.LocalDate

interface SpendingReportCreation {

    fun createReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate,
        categories: Set<StringId<SpendingCategory>>? = null
    ): SpendingReport
}