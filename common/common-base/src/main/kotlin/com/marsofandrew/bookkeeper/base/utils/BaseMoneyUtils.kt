package com.marsofandrew.bookkeeper.base.utils

import com.marsofandrew.bookkeeper.properties.BaseMoney

@Suppress("UNCHECKED_CAST")
fun <T : BaseMoney> summarize(left: Collection<T>, right: Collection<T>): Collection<T> {
    val rightTotalByCurrency = right.groupBy { it.currency }
        .mapValues { (_, moneys) -> moneys.single() }

    val leftTotalByCurrency = left.groupBy { it.currency }
        .mapValues { (_, moneys) -> moneys.single() }

    return (leftTotalByCurrency.keys + rightTotalByCurrency.keys)
        .mapNotNull { currency ->
            sumOfNullable(
                leftTotalByCurrency[currency],
                rightTotalByCurrency[currency]
            ) { left, right -> (left + right) as T }
        }
}

fun <T> sumOfNullable(left: T?, right: T?, summarization: (T, T) -> T): T? {
    if (left == null && right == null) return null

    if (left == null) return right
    if (right == null) return left

    return summarization(left, right)
}