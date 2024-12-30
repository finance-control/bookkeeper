package com.marsofandrew.bookkeeper.tokens.encryption

interface TokenEncryptor {
    
    fun encrypt(text: String): String
    
    fun decrypt(text: String): String
}