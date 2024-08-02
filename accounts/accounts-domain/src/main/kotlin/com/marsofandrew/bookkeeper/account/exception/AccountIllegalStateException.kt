package com.marsofandrew.bookkeeper.account.exception

class AccountIllegalStateException(message: String) : RuntimeException(message)

internal fun validateAccount(predicate: Boolean, lazyMessage: () -> String) {
    if (!predicate) {
        throw AccountIllegalStateException(lazyMessage())
    }
}
