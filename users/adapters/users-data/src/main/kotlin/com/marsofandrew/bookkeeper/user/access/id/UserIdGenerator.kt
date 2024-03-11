package com.marsofandrew.bookkeeper.user.access.id

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User

internal interface UserIdGenerator {

    fun generateId(): NumericId<User>
}