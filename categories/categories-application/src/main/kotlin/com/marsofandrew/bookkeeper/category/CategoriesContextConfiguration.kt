package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.impl.CategoryAddingImpl
import com.marsofandrew.bookkeeper.category.impl.CategoryDeletionImpl
import com.marsofandrew.bookkeeper.category.impl.CategorySelectionImpl
import com.marsofandrew.bookkeeper.category.impl.CategoryValidationImpl
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
}
