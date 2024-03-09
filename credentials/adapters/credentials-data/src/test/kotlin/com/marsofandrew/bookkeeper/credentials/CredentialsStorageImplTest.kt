package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorageImpl
import com.marsofandrew.bookkeeper.credentials.fixtures.CredentialsFixtureLoader
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [CredentialsFixtureLoader::class, CredentialsStorageImpl::class])
internal class CredentialsStorageImplTest {

    @Autowired
    private lateinit var credentialsFixtureLoader: CredentialsFixtureLoader

    @Autowired
    private lateinit var credentialsStorageImpl: CredentialsStorageImpl
}