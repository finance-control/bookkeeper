package com.marsofandrew.bookkeeper.base.model

import com.marsofandrew.bookkeeper.properties.exception.validateFiled

data class Version(
    val value: Int
) {
    init {
        validateFiled(value >= 0) { "Version is negative" }
    }
}

fun Int.asVersion() = Version(this)
fun Long.asVersion() = Version(this.toInt())
