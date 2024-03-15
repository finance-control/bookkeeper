package com.marsofandrew.bookkeeper.category

interface CategoryAdding<T : UserCategory<T>> {

    fun add(category: T): T
}