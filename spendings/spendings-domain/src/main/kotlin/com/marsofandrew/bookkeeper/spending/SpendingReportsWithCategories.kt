package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.category.Category

data class SpendingReportsWithCategories(
    val report: SpendingReport,
    val categories: Map<NumericId<Category>, Category>,
)
