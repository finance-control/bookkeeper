package com.marsofandrew.bookkeeper.properties.id

import com.marsofandrew.bookkeeper.properties.exception.validateFiled

data class NumericId<T>(
    private val inner: Long?
) : AbstractId<Long>(inner) {

    init {
        validateFiled(inner == null || inner > 0) { "id should be either null or positive" }
    }

    companion object {
        fun <T> unidentified() = NumericId<T>(null)
    }
}

fun <T> Long.asId(): NumericId<T> = NumericId(this)
fun <T> Int.asId(): NumericId<T> = NumericId(this.toLong())