package com.marsofandrew.bookkeeper.auth

interface UserAuthentication {

    fun authenticate(authKey: String): Long
}
