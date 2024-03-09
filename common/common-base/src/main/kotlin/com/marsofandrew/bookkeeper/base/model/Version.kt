package com.marsofandrew.bookkeeper.base.model

data class Version(
    val value: Int
) {
    init {
        check(value >= 0) { "Version is negative" }
    }
}
