package com.marsofandrew.bookkeeper.tokens.encryption

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
internal class TokensEncryptorImpl(
    @Value("\${tokens.encryption.secret_key}") private val secretKey: String,
) : TokenEncryptor {

    init {
        require(secretKey.length == 32) { "SecretKey must have a length of 32 characters" }
    }

    private val key = SecretKeySpec(secretKey.toByteArray(), "AES")
    private val cipher = Cipher.getInstance("AES")

    override fun encrypt(text: String): String {
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return Base64.getEncoder().encode(cipher.doFinal(text.toByteArray())).toString(Charsets.UTF_8)
    }

    override fun decrypt(text: String): String {
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher.doFinal(Base64.getDecoder().decode(text)).toString(Charsets.UTF_8)
    }
}
