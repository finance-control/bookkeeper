package com.marsofandrew.bookkeeper.spending.exception

import com.marsofandrew.bookkeeper.base.exception.ValidationException
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.category.SpendingCategory

data class InvalidCategoryException(val categoryId: NumericId<SpendingCategory>) :
    ValidationException("Category with id: ${categoryId.value} does not exist")