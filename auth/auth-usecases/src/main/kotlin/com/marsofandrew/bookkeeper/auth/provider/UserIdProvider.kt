package com.marsofandrew.bookkeeper.auth.provider

interface UserIdProvider {

    fun getIdByKey(authKey: String): Long
}