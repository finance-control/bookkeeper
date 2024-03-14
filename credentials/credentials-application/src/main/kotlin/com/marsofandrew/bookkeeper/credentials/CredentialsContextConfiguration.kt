package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorage
import com.marsofandrew.bookkeeper.credentials.encoder.CredentialsEncoder
import com.marsofandrew.bookkeeper.credentials.impl.CredentialsSettingImpl
import com.marsofandrew.bookkeeper.credentials.impl.CredentialsUserIdSelectionImpl
import java.time.Clock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class CredentialsContextConfiguration {

    @Bean
    fun credentialsSetting(
        credentialsEncoder: CredentialsEncoder,
        credentialsStorage: CredentialsStorage,
        clock: Clock,
        transactionExecutor: TransactionExecutor
    ): CredentialsSetting =
        CredentialsSettingImpl(credentialsEncoder, credentialsStorage, clock, transactionExecutor)

    @Bean
    fun credentialsUserIdSelection(
        credentialsEncoder: CredentialsEncoder,
        credentialsStorage: CredentialsStorage
    ): CredentialsUserIdSelection = CredentialsUserIdSelectionImpl(credentialsEncoder, credentialsStorage)
}