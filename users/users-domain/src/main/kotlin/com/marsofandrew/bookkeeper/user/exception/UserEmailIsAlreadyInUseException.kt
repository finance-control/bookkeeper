package com.marsofandrew.bookkeeper.user.exception

import com.marsofandrew.bookkeeper.properties.email.Email

class UserEmailIsAlreadyInUseException(val email: Email, cause: Throwable) : RuntimeException(cause)