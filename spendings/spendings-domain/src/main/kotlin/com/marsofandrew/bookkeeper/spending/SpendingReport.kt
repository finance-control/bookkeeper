package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.category.SpendingCategory

data class SpendingReport(
    val spendingByCategory: Map<NumericId<SpendingCategory>, List<PositiveMoney>>,
    val total: List<PositiveMoney>
)
