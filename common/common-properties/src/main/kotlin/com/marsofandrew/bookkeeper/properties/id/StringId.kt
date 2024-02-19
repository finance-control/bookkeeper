package com.marsofandrew.bookkeeper.properties.id

data class StringId<T>(
    private val inner: String?
) : Id<String> {

    init {
        check(inner == null || inner.isNotBlank()) { "StringId could not be blank or empty" }
    }

    override val value: String
        get() = requireNotNull(inner)

    override val initialized: Boolean
        get() = inner != null

    companion object {
        fun <T> unidentified() = StringId<T>(null)
    }
}

fun <T> String.asId(): StringId<T> = StringId(this)