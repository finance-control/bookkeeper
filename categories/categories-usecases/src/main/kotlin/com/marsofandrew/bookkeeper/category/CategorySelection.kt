package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategorySelection {

    fun select(userId: NumericId<User>, ids: Set<NumericId<UserCategory>>): List<UserCategory>
    fun select(userId: NumericId<User>): List<UserCategory>
    fun select(userId: NumericId<User>, title: String): UserCategory
}