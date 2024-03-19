package com.marsofandrew.bookkeeper.report.common

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.user.User

interface WithUserId {
    val userId: NumericId<User>
}