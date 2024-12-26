package com.marsofandrew.bookkeeper.tokens.hash

interface HashGenerator {

    fun hash(input: ByteArray): ByteArray
}
