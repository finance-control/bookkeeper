package com.marsofandrew.bookkeeper.transfer.exception

import com.marsofandrew.bookkeeper.properties.exception.ValidationException
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfer.category.TransferCategory

data class InvalidCategoryException(val categoryId: NumericId<TransferCategory>) :
    ValidationException("Category with id: ${categoryId.value} does not exist")