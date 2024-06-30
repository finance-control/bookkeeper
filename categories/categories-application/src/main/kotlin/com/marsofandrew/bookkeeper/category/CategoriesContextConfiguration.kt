package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.impl.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class CategoriesContextConfiguration {

    @Bean
    fun categoryAdding(
        categoryStorage: CategoryStorage
    ): CategoryAdding = CategoryAddingImpl(categoryStorage)


    @Bean
    fun categoryDeletion(
        categoryStorage: CategoryStorage
    ): CategoryDeletion = CategoryDeletionImpl(categoryStorage)

    @Bean
    fun categorySelection(
        categoryStorage: CategoryStorage
    ): CategorySelection = CategorySelectionImpl(categoryStorage)

    @Bean
    fun categoryValidation(
        categoryStorage: CategoryStorage
    ): CategoryValidation = CategoryValidationImpl(categoryStorage)

    @Bean
    fun categoryModification(
        categoryStorage: CategoryStorage,
        transactionExecutor: TransactionExecutor
    ): CategoryModification = CategoryModificationImpl(categoryStorage, transactionExecutor)
}
