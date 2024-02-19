package com.marsofandrew.bookkeeper.spendings

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spendings.category.SpendingCategory

data class SpendingReport(
    val spendingByCategory: Map<StringId<SpendingCategory>, List<PositiveMoney>>,
    val total: List<PositiveMoney>
)
