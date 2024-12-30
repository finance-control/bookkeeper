package com.marsofandrew.bookkeeper.credentials.encryptor

interface CredentialsEncryptor {

    fun encode(value: String): String

    fun matches(rawPassword: String, encodedPassword: String): Boolean
}
