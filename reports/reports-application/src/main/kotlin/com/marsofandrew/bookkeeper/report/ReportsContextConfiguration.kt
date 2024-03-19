package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
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
        transactionExecutor: TransactionExecutor,
    ): ReportEarningAdding = ReportEarningAddingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    @Bean
    fun reportEarningRemoving(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ): ReportEarningRemoving = ReportEarningRemovingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    @Bean
    fun reportSpendingAdding(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ): ReportSpendingAdding = ReportSpendingAddingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    @Bean
    fun reportSpendingRemoving(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ): ReportSpendingRemoving = ReportSpendingRemovingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    @Bean
    fun reportTransferAdding(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ): ReportTransferAdding = ReportTransferAddingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    @Bean
    fun reportTransferRemoving(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ): ReportTransferRemoving = ReportTransferRemovingImpl(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )
}
