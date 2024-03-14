package com.marsofandrew.bookkeeper.auth.impl

class FakeUserAuthenticationImpl : AbstractUserAuthenticationImpl() {

    override fun getUserIdByAuth(authKey: String): Long {
        return authKey.toLong()
    }
}
