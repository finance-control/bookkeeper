package com.marsofandrew.bookkeeper.categories

import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface UserCategory<T: UserCategory<T>> {
    val id: NumericId<T>
    val userId: NumericId<User>
    val title: String
    val description: String?
}
