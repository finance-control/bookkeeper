package com.marsofandrew.bookkeeper.auth.exception

class ForbiddenException(cause : Throwable): Exception(cause)

fun forbidden(cause: Throwable) = ForbiddenException(cause)