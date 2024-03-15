package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.impl.spending.SpendingCategoryAddingImpl
import com.marsofandrew.bookkeeper.category.impl.spending.SpendingCategoryDeletionImpl
import com.marsofandrew.bookkeeper.category.impl.spending.SpendingCategorySelectionImpl
import com.marsofandrew.bookkeeper.category.impl.spending.SpendingCategoryValidationImpl
import com.marsofandrew.bookkeeper.category.impl.transfer.TransferCategoryAddingImpl
import com.marsofandrew.bookkeeper.category.impl.transfer.TransferCategoryDeletionImpl
import com.marsofandrew.bookkeeper.category.impl.transfer.TransferCategorySelectionImpl
import com.marsofandrew.bookkeeper.category.impl.transfer.TransferCategoryValidationImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class CategoriesContextConfiguration {

    @Bean
    fun spendingCategoryAdding(
        spendingCategoryStorage: CategoryStorage<SpendingUserCategory>
    ): CategoryAdding<SpendingUserCategory> = SpendingCategoryAddingImpl(spendingCategoryStorage)


    @Bean
    fun spendingCategoryDeletion(
        spendingCategoryStorage: CategoryStorage<SpendingUserCategory>
    ): CategoryDeletion<SpendingUserCategory> = SpendingCategoryDeletionImpl(spendingCategoryStorage)

    @Bean
    fun spendingCategorySelection(
        spendingCategoryStorage: CategoryStorage<SpendingUserCategory>
    ): CategorySelection<SpendingUserCategory> = SpendingCategorySelectionImpl(spendingCategoryStorage)

    @Bean
    fun spendingCategoryValidation(
        spendingCategoryStorage: CategoryStorage<SpendingUserCategory>
    ): CategoryValidation<SpendingUserCategory> = SpendingCategoryValidationImpl(spendingCategoryStorage)

    @Bean
    fun transferCategoryAdding(
        spendingCategoryStorage: CategoryStorage<TransferUserCategory>
    ): CategoryAdding<TransferUserCategory> = TransferCategoryAddingImpl(spendingCategoryStorage)


    @Bean
    fun transferCategoryDeletion(
        spendingCategoryStorage: CategoryStorage<TransferUserCategory>
    ): CategoryDeletion<TransferUserCategory> = TransferCategoryDeletionImpl(spendingCategoryStorage)

    @Bean
    fun transferCategorySelection(
        spendingCategoryStorage: CategoryStorage<TransferUserCategory>
    ): CategorySelection<TransferUserCategory> = TransferCategorySelectionImpl(spendingCategoryStorage)

    @Bean
    fun transferCategoryValidation(
        transferCategoryStorage: CategoryStorage<TransferUserCategory>
    ): CategoryValidation<TransferUserCategory> = TransferCategoryValidationImpl(transferCategoryStorage)
}
