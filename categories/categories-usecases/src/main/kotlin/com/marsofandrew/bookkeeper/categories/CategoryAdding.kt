package com.marsofandrew.bookkeeper.categories

interface CategoryAdding<T : UserCategory<T>> {

    fun add(category: T): T
}