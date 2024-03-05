package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.user.User

interface YearlyUserReportSelection {

    fun select(userId: NumericId<User>): List<YearlyUserReport>
}