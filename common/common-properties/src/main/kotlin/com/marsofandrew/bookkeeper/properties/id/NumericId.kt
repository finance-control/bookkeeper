package com.marsofandrew.bookkeeper.properties.id

data class NumericId<T>(
    private val inner: Long?
) : Id<Long> {

    override val value: Long
        get() = requireNotNull(inner)

    override val initialized: Boolean
        get() = inner != null

    companion object {
        fun <T> unidentified() = NumericId<T>(null)
    }
}

fun <T> Long.asId(): NumericId<T> = NumericId(this)
fun <T> Int.asId(): NumericId<T> = NumericId(this.toLong())