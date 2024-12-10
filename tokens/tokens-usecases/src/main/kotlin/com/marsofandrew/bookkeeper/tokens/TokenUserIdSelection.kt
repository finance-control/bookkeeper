package com.marsofandrew.bookkeeper.tokens

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.tokens.user.User

interface TokenUserIdSelection {

    fun select(clientId: String, ipAddress: String?, token: String): NumericId<User>?
}