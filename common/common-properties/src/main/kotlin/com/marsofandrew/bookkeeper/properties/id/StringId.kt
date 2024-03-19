package com.marsofandrew.bookkeeper.properties.id

import com.marsofandrew.bookkeeper.properties.exception.validateFiled

data class StringId<T>(
    private val inner: String?
) : AbstractId<String>(inner) {

    init {
        validateFiled(inner == null || inner.isNotBlank()) { "StringId could not be blank or empty" }
    }

    companion object {
        fun <T> unidentified() = StringId<T>(null)
    }
}

fun <T> String.asId(): StringId<T> = StringId(this)