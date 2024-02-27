package com.marsofandrew.bookkeeper.spendings

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spendings.category.SpendingCategory

data class SpendingReport(
    val spendingByCategory: Map<NumericId<SpendingCategory>, List<PositiveMoney>>,
    val total: List<PositiveMoney>
)
