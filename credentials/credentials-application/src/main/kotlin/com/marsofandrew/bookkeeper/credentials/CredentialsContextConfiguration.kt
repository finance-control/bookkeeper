package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorage
import com.marsofandrew.bookkeeper.credentials.encoder.CredentialsEncoder
import com.marsofandrew.bookkeeper.credentials.impl.CredentialsModificationImpl
import com.marsofandrew.bookkeeper.credentials.impl.CredentialsSettingImpl
import com.marsofandrew.bookkeeper.credentials.impl.CredentialsUserIdSelectionImpl
import com.marsofandrew.bookkeeper.credentials.impl.UserEmailSelectionImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

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

    @Bean
    fun userEmailSelection(credentialsStorage: CredentialsStorage): UserEmailSelection =
        UserEmailSelectionImpl(credentialsStorage)

    @Bean
    fun credentialsModification(
        credentialsEncoder: CredentialsEncoder,
        credentialsStorage: CredentialsStorage,
        clock: Clock,
        transactionExecutor: TransactionExecutor
    ): CredentialsModification =
        CredentialsModificationImpl(
            credentialsEncoder,
            credentialsStorage,
            clock,
            transactionExecutor
        )
}