package com.marsofandrew.bookkeeper.auth.provider

interface UserIdByTokenProvider {

    fun getIdByToken(token: String, ip: String, clientId: String): Long?
}
