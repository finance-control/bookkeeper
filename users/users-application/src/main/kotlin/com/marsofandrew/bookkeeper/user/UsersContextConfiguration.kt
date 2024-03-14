package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.credentials.UserCredentialsSetter
import com.marsofandrew.bookkeeper.user.impl.UserRegistrationImpl
import java.time.Clock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class UsersContextConfiguration {

    @Bean
    fun userRegistration(
        userStorage: UserStorage,
        userCredentialsSetter: UserCredentialsSetter,
        transactionExecutor: TransactionExecutor,
        clock: Clock
    ): UserRegistration = UserRegistrationImpl(
        userStorage,
        userCredentialsSetter,
        transactionExecutor,
        clock
    )
}