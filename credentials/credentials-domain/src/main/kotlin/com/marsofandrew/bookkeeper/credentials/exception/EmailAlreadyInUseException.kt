package com.marsofandrew.bookkeeper.credentials.exception

import com.marsofandrew.bookkeeper.properties.email.Email

class EmailAlreadyInUseException(val email: Email) : RuntimeException("Email ${email.value} is already in use")