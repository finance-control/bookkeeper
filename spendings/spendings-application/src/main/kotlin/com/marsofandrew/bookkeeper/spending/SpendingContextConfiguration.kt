package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.account.SpendingAccountValidator
import com.marsofandrew.bookkeeper.spending.category.SpendingCategoryValidator
import com.marsofandrew.bookkeeper.spending.impl.*
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
    ): SpendingAdding =
        SpendingAddingImpl(spendingStorage, eventPublisher, spendingCategoryValidator, spendingAccountValidator)

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

    @Bean
    fun spendingModification(
        spendingStorage: SpendingStorage,
        eventPublisher: EventPublisher,
        transactionExecutor: TransactionExecutor,
        spendingCategoryValidator: SpendingCategoryValidator,
        spendingAccountValidator: SpendingAccountValidator
    ): SpendingModification = SpendingModificationImpl(
        spendingStorage,
        eventPublisher,
        transactionExecutor,
        spendingCategoryValidator,
        spendingAccountValidator
    )
}