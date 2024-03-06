package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.impl.DailyUserReportAggregationImpl
import com.marsofandrew.bookkeeper.report.impl.DailyUserReportSelectionImpl
import com.marsofandrew.bookkeeper.report.impl.MonthlyUserReportAggregationImpl
import com.marsofandrew.bookkeeper.report.impl.MonthlyUserReportSelectionImpl
import com.marsofandrew.bookkeeper.report.impl.ReportEarningAddingImpl
import com.marsofandrew.bookkeeper.report.impl.ReportEarningRemovingImpl
import com.marsofandrew.bookkeeper.report.impl.ReportSpendingAddingImpl
import com.marsofandrew.bookkeeper.report.impl.ReportSpendingRemovingImpl
import com.marsofandrew.bookkeeper.report.impl.ReportTransferAddingImpl
import com.marsofandrew.bookkeeper.report.impl.ReportTransferRemovingImpl
import com.marsofandrew.bookkeeper.report.impl.YearlyUserReportSelectionImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class ReportsContextConfiguration {


    @Bean
    fun dailyUserReportAggregation(
        dailyUserReportStorage: DailyUserReportStorage
    ): DailyUserReportAggregation = DailyUserReportAggregationImpl(dailyUserReportStorage)

    @Bean
    fun dailyUserReportSelection(
        dailyUserReportStorage: DailyUserReportStorage
    ): DailyUserReportSelection = DailyUserReportSelectionImpl(dailyUserReportStorage)

    @Bean
    fun monthlyUserReportAggregation(
        monthlyUserReportStorage: MonthlyUserReportStorage
    ): MonthlyUserReportAggregation = MonthlyUserReportAggregationImpl(monthlyUserReportStorage)

    @Bean
    fun monthlyUserReportSelection(
        monthlyUserReportStorage: MonthlyUserReportStorage
    ): MonthlyUserReportSelection = MonthlyUserReportSelectionImpl(monthlyUserReportStorage)

    @Bean
    fun yearlyUserReportSelection(
        yearlyUserReportStorage: YearlyUserReportStorage
    ): YearlyUserReportSelection = YearlyUserReportSelectionImpl(yearlyUserReportStorage)

    @Bean
    fun reportEarningAdding(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionalExecution: TransactionalExecution,
    ): ReportEarningAdding = ReportEarningAddingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionalExecution
    )

    @Bean
    fun reportEarningRemoving(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionalExecution: TransactionalExecution,
    ): ReportEarningRemoving = ReportEarningRemovingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionalExecution
    )

    @Bean
    fun reportSpendingAdding(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionalExecution: TransactionalExecution,
    ): ReportSpendingAdding = ReportSpendingAddingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionalExecution
    )

    @Bean
    fun reportSpendingRemoving(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionalExecution: TransactionalExecution,
    ): ReportSpendingRemoving = ReportSpendingRemovingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionalExecution
    )

    @Bean
    fun reportTransferAdding(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionalExecution: TransactionalExecution,
    ): ReportTransferAdding = ReportTransferAddingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionalExecution
    )

    @Bean
    fun reportTransferRemoving(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionalExecution: TransactionalExecution,
    ): ReportTransferRemoving = ReportTransferRemovingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionalExecution
    )
}