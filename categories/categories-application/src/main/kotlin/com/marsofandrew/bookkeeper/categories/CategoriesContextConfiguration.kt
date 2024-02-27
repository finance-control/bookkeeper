package com.marsofandrew.bookkeeper.categories

import com.marsofandrew.bookkeeper.categories.access.CategoryStorage
import com.marsofandrew.bookkeeper.categories.impl.spending.SpendingCategoryAddingImpl
import com.marsofandrew.bookkeeper.categories.impl.spending.SpendingCategoryDeletionImpl
import com.marsofandrew.bookkeeper.categories.impl.spending.SpendingCategorySelectionImpl
import com.marsofandrew.bookkeeper.categories.impl.transfer.TransferCategoryAddingImpl
import com.marsofandrew.bookkeeper.categories.impl.transfer.TransferCategoryDeletionImpl
import com.marsofandrew.bookkeeper.categories.impl.transfer.TransferCategorySelectionImpl
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
}