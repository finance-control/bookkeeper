package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.account.SpendingAccountValidator
import com.marsofandrew.bookkeeper.spending.category.SpendingCategoryValidator
import com.marsofandrew.bookkeeper.spending.impl.SpendingAddingImpl
import com.marsofandrew.bookkeeper.spending.impl.SpendingDeletionImpl
import com.marsofandrew.bookkeeper.spending.impl.SpendingReportCreationImpl
import com.marsofandrew.bookkeeper.spending.impl.SpendingSelectionImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpendingContextConfiguration {

    @Bean
    fun addingSpending(
        spendingStorage: SpendingStorage,
        eventPublisher: EventPublisher,
        spendingCategoryValidator: SpendingCategoryValidator,
        spendingAccountValidator: SpendingAccountValidator
    ): SpendingAdding = SpendingAddingImpl(spendingStorage, eventPublisher, spendingCategoryValidator, spendingAccountValidator)

    @Bean
    fun creatingSpendingReport(
        spendingStorage: SpendingStorage
    ): SpendingReportCreation = SpendingReportCreationImpl(spendingStorage)

    @Bean
    fun deletingSpending(
        spendingStorage: SpendingStorage,
        eventPublisher: EventPublisher
    ): SpendingDeletion = SpendingDeletionImpl(spendingStorage, eventPublisher)

    @Bean
    fun selectingSpending(
        spendingStorage: SpendingStorage
    ): SpendingSelection = SpendingSelectionImpl(spendingStorage)
}