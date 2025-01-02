package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.account.SpendingAccountValidator
import com.marsofandrew.bookkeeper.spending.category.CategorySelector
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
        categorySelector: CategorySelector,
        spendingCategoryValidator: SpendingCategoryValidator,
        spendingAccountValidator: SpendingAccountValidator
    ): SpendingAdding =
        SpendingAddingImpl(spendingStorage, eventPublisher, categorySelector, spendingCategoryValidator, spendingAccountValidator)

    @Bean
    fun creatingSpendingReport(
        spendingStorage: SpendingStorage,
        categorySelector: CategorySelector,
    ): SpendingReportCreation = SpendingReportCreationImpl(spendingStorage, categorySelector)

    @Bean
    fun deletingSpending(
        spendingStorage: SpendingStorage,
        eventPublisher: EventPublisher
    ): SpendingDeletion = SpendingDeletionImpl(spendingStorage, eventPublisher)

    @Bean
    fun selectingSpending(
        spendingStorage: SpendingStorage,
        categorySelector: CategorySelector,
    ): SpendingSelection = SpendingSelectionImpl(spendingStorage, categorySelector)

    @Bean
    fun spendingModification(
        spendingStorage: SpendingStorage,
        eventPublisher: EventPublisher,
        categorySelector: CategorySelector,
        transactionExecutor: TransactionExecutor,
        spendingCategoryValidator: SpendingCategoryValidator,
        spendingAccountValidator: SpendingAccountValidator
    ): SpendingModification = SpendingModificationImpl(
        spendingStorage,
        eventPublisher,
        transactionExecutor,
        categorySelector,
        spendingCategoryValidator,
        spendingAccountValidator
    )
}