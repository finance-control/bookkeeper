package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.YearlyUserReportSelection
import com.marsofandrew.bookkeeper.report.access.YearlyReportStorage
import com.marsofandrew.bookkeeper.report.user.User

class YearlyUserReportSelectionImpl(
    private val yearlyReportStorage: YearlyReportStorage
) : YearlyUserReportSelection {

    override fun select(userId: NumericId<User>): List<YearlyUserReport> {
        return yearlyReportStorage.findAllByUserId(userId)
    }
}