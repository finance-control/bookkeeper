package com.marsofandrew.bookkeeper.properties.id

data class ObjectId<T>(
    private val inner: T?
) : AbstractId<T>(inner)
