package com.marsofandrew.bookkeeper.properties

enum class Currency(
    val fullName: String,
    val code: String
) {
    EUR("Euro", "EUR"),
    RUB("Russian Ruble", "RUB"),
    USD("US Dollar", "USD");

    companion object {
        fun byCodeOrThrow(code: String): Currency = entries
            .firstOrNull { currency -> currency.code == code } ?: throw Exception("WTF 1") // TODO: fix it
    }
}