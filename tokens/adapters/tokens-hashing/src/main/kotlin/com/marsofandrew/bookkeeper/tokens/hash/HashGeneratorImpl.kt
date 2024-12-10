package com.marsofandrew.bookkeeper.tokens.hash

import org.springframework.stereotype.Service
import java.security.MessageDigest

@Service
internal class HashGeneratorImpl(
    private val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
) : HashGenerator {

    override fun hash(input: ByteArray): ByteArray {
        return messageDigest.digest(input)
    }
}