package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.spending.category.Category

data class SpendingWithCategory(
    val spending: Spending,
    val category: Category,
)
