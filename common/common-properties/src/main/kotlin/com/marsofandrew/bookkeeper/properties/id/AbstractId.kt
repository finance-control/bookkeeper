package com.marsofandrew.bookkeeper.properties.id

abstract class AbstractId<T>(
    private val inner: T?,
) : Id<T> {

    override val value: T
        get() = requireNotNull(inner)

    override val initialized: Boolean
        get() = inner != null

    override fun toString(): String = "{$inner}"
}