package com.marsofandrew.bookkeeper.properties.id

interface Id<T> {
    val value: T
    val initialized: Boolean
    val rawValue: T?
}