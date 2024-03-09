package com.marsofandrew.bookkeeper.credentials.encoder

interface CredentialsEncoder {

    fun encode(value: String): String

    fun matches(rawPassword: String, encodedPassword: String): Boolean
}