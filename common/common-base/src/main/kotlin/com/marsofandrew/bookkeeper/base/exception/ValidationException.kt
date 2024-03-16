package com.marsofandrew.bookkeeper.base.exception

open class ValidationException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}
