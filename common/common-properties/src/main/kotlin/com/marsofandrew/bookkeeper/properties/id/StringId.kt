package com.marsofandrew.bookkeeper.properties.id

data class StringId<T>(
    private val inner: String?
) : AbstractId<String>(inner) {

    init {
        check(inner == null || inner.isNotBlank()) { "StringId could not be blank or empty" }
    }

    companion object {
        fun <T> unidentified() = StringId<T>(null)
    }
}

fun <T> String.asId(): StringId<T> = StringId(this)