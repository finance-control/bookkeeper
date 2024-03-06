package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.impl.DailyUserReportAggregationImpl
import com.marsofandrew.bookkeeper.report.impl.MonthlyUserReportAggregationImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class ReportsContextConfiguration {


    @Bean
    fun dailyReportAggregation(
        dailyUserReportStorage: DailyUserReportStorage
    ): DailyUserReportAggregation = DailyUserReportAggregationImpl(dailyUserReportStorage)

    fun dailyUserReportSelection(
        dailyUserReportStorage: DailyUserReportStorage
    ): DailyUserReportSelection

    @Bean
    fun monthlyReportAggregation(
        monthlyUserReportStorage: MonthlyUserReportStorage
    ): MonthlyUserReportAggregation = MonthlyUserReportAggregationImpl(monthlyUserReportStorage)
}