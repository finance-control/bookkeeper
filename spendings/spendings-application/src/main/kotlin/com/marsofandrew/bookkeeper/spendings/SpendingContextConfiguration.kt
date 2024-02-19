package com.marsofandrew.bookkeeper.spendings

import com.marsofandrew.bookkeeper.spendings.access.SpendingStorage
import com.marsofandrew.bookkeeper.spendings.impl.SpendingAddingImpl
import com.marsofandrew.bookkeeper.spendings.impl.SpendingReportCreationImpl
import com.marsofandrew.bookkeeper.spendings.impl.SpendingDeletionImpl
import com.marsofandrew.bookkeeper.spendings.impl.SpendingSelectionImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpendingContextConfiguration {

    @Bean
    fun addingSpending(
        spendingStorage: SpendingStorage
    ): SpendingAdding = SpendingAddingImpl(spendingStorage)

    @Bean
    fun creatingSpendingReport(
        spendingStorage: SpendingStorage
    ): SpendingReportCreation = SpendingReportCreationImpl(spendingStorage)

    @Bean
    fun deletingSpending(
        spendingStorage: SpendingStorage
    ): SpendingDeletion = SpendingDeletionImpl(spendingStorage)

    @Bean
    fun selectingSpending(
        spendingStorage: SpendingStorage
    ): SpendingSelection = SpendingSelectionImpl(spendingStorage)
}