package com.marsofandrew.bookkeeper.credentials.fixtures

import com.marsofandrew.bookkeeper.credentials.repository.CredentialsRepository
import org.springframework.stereotype.Service

@Service
internal class CredentialsFixtureLoader(
    val credentialsRepository: CredentialsRepository
) {

    fun load(block: CredentialsFixtureLoader.() -> Unit) = this.block()

    fun clean() {
        credentialsRepository.deleteAll()
    }
}