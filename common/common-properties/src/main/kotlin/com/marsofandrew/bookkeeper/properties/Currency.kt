package com.marsofandrew.bookkeeper.properties

enum class Currency(
    val title: String,
    val code: String
) {
    EUR("Euro", "EUR"),
    RUB("Russian Ruble", "RUB"),
    USD("US Dollar", "USD"),
    GBP("GB Pound", "GBP"),
    GEL("Georgian Lari", "GEL"),
    AED("Arab Emirates Dirham", "AED"),
    RSD("Republic of Serbia Dinar", "RSD");

    companion object {
        fun byCodeOrThrow(code: String): Currency = entries
            .firstOrNull { currency -> currency.code == code } ?: throw IllegalArgumentException(code)
    }
}
