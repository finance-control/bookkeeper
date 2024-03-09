package com.marsofandrew.bookkeeper.credentials.fixtures

import com.marsofandrew.bookkeeper.credentials.UserCredentials
import com.marsofandrew.bookkeeper.credentials.entity.toCredentialsEntity

internal fun CredentialsFixtureLoader.credentials(credentials: UserCredentials) =
    credentialsRepository.save(credentials.toCredentialsEntity())
