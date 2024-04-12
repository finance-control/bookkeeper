package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.category.Category

data class SpendingReport(
    val spendingByCategory: Map<NumericId<Category>, List<PositiveMoney>>,
    val total: List<PositiveMoney>
)
