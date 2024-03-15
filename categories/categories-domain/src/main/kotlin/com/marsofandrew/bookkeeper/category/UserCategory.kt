package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface UserCategory<T : UserCategory<T>>: DomainModel {
    override val id: NumericId<T>
    val userId: NumericId<User>
    val title: String
    val description: String?
}
