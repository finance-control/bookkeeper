package com.marsofandrew.bookkeeper.auth.provider

import com.marsofandrew.bookkeeper.properties.email.Email

interface UserIdByCredentialsProvider {

    fun getIdByKey(email: Email, password: String): Long?
}