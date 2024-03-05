package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.base.utils.sumOfNullable
import com.marsofandrew.bookkeeper.base.utils.summarize
import com.marsofandrew.bookkeeper.properties.BaseMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId

data class Report<Category, MoneyType : BaseMoney>(
    val byCategory: Map<NumericId<Category>, List<MoneyType>>,
    val total: List<MoneyType>
) {

    operator fun plus(
        other: Report<Category, MoneyType>
    ): Report<Category, MoneyType> {
        val resultByCategory = (byCategory.keys + other.byCategory.keys).associateWith { category ->
            val left = byCategory[category]
            val right = other.byCategory[category]
            sumOfNullable(left, right) { l, r -> summarize(l, r).toList() }!!
        }

        return Report(
            byCategory = resultByCategory,
            total = summarize(total, other.total).toList()
        )
    }
    companion object {

        fun <Category, MoneyType : BaseMoney> empty() = Report<Category, MoneyType>(
            byCategory = emptyMap(),
            total = emptyList()
        )
    }
}
