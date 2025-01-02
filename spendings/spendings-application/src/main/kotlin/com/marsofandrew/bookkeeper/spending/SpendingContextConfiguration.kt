package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.account.SpendingAccountValidator
import com.marsofandrew.bookkeeper.spending.category.SpendingCategorySelector
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
        spendingCategorySelector: SpendingCategorySelector,
        spendingCategoryValidator: SpendingCategoryValidator,
        spendingAccountValidator: SpendingAccountValidator
    ): SpendingAdding =
        SpendingAddingImpl(spendingStorage, eventPublisher, spendingCategorySelector, spendingCategoryValidator, spendingAccountValidator)

    @Bean
    fun creatingSpendingReport(
        spendingStorage: SpendingStorage,
        spendingCategorySelector: SpendingCategorySelector,
    ): SpendingReportCreation = SpendingReportCreationImpl(spendingStorage, spendingCategorySelector)

    @Bean
    fun deletingSpending(
        spendingStorage: SpendingStorage,
        eventPublisher: EventPublisher
    ): SpendingDeletion = SpendingDeletionImpl(spendingStorage, eventPublisher)

    @Bean
    fun selectingSpending(
        spendingStorage: SpendingStorage,
        spendingCategorySelector: SpendingCategorySelector,
    ): SpendingSelection = SpendingSelectionImpl(spendingStorage, spendingCategorySelector)

    @Bean
    fun spendingModification(
        spendingStorage: SpendingStorage,
        eventPublisher: EventPublisher,
        spendingCategorySelector: SpendingCategorySelector,
        transactionExecutor: TransactionExecutor,
        spendingCategoryValidator: SpendingCategoryValidator,
        spendingAccountValidator: SpendingAccountValidator
    ): SpendingModification = SpendingModificationImpl(
        spendingStorage,
        eventPublisher,
        transactionExecutor,
        spendingCategorySelector,
        spendingCategoryValidator,
        spendingAccountValidator
    )
}