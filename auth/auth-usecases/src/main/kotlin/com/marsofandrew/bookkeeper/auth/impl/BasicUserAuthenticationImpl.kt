package com.marsofandrew.bookkeeper.auth.impl

import com.marsofandrew.bookkeeper.auth.exception.IncorrectCredentialsException
import com.marsofandrew.bookkeeper.auth.exception.forbidden
import com.marsofandrew.bookkeeper.auth.provider.UserIdByCredentialsProvider
import com.marsofandrew.bookkeeper.properties.email.Email
import java.util.Base64

class BasicUserAuthenticationImpl(
    private val userIdByCredentialsProvider: UserIdByCredentialsProvider
) : AbstractUserAuthenticationImpl() {

    override fun getUserIdByAuth(authKey: String): Long {
        val params = kotlin.runCatching { String(Base64.getDecoder().decode(authKey)) }
            .getOrElse { authKey }
            .split(":", limit = 2)
        if (params.size < 2) throw forbidden(IllegalArgumentException("Password is not provided"))
        val email = Email(params[0])
        val rawPassword = params[1]

        return userIdByCredentialsProvider.getIdByCredentials(email, rawPassword)
            ?: throw IncorrectCredentialsException("Incorrect credentials")
    }
}
