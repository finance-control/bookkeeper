package com.marsofandrew.bookkeeper.properties.exception

class ObjectCreateValidationException(
    message: String
) : ValidationException(message)

inline fun <reified T> T.validateFiled(predicate: Boolean, cause: () -> String) {
    if (!predicate) {
        throw ObjectCreateValidationException("${T::class.qualifiedName} creation failed, caused by: ${cause()}")
    }
}
