package com.marsofandrew.bookkeeper.spending.impl.util

import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.account.SpendingAccountValidator
import com.marsofandrew.bookkeeper.spending.category.SpendingCategoryValidator
import com.marsofandrew.bookkeeper.spending.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.spending.exception.InvalidCategoryException

internal class SpendingValidator(
    private val spendingCategoryValidator: SpendingCategoryValidator,
    private val spendingAccountValidator: SpendingAccountValidator
) {

    fun validate(spending: Spending) {
        if (!spendingCategoryValidator.validate(spending.userId, spending.categoryId)) {
            throw InvalidCategoryException(spending.categoryId)
        }

        if (spending.sourceAccountId?.let { spendingAccountValidator.validate(spending.userId, it) } == false) {
            throw InvalidAccountException(spending.sourceAccountId!!)
        }
    }
}