package com.marsofandrew.bookkeeper.user.exception

import com.marsofandrew.bookkeeper.properties.exception.ValidationException
import com.marsofandrew.bookkeeper.properties.email.Email

class UserEmailIsAlreadyInUseException(val email: Email, cause: Throwable) : ValidationException(cause)
