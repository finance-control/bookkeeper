package com.marsofandrew.bookkeeper.category.exception

import com.marsofandrew.bookkeeper.properties.exception.ValidationException

class CategoryAlreadyExistsException(message: String) : ValidationException(message)