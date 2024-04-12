package com.marsofandrew.bookkeeper.spending.exception

import com.marsofandrew.bookkeeper.properties.exception.ValidationException
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.category.Category

data class InvalidCategoryException(val categoryId: NumericId<Category>) :
    ValidationException("Category with id: ${categoryId.value} does not exist")