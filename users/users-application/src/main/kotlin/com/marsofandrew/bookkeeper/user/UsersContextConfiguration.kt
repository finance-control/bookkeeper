package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.credentials.UserCredentialsSetter
import com.marsofandrew.bookkeeper.user.credentials.UserEmailSelector
import com.marsofandrew.bookkeeper.user.impl.UserLoginImpl
import com.marsofandrew.bookkeeper.user.impl.UserRegistrationImpl
import com.marsofandrew.bookkeeper.user.impl.UserSelectionImpl
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

    @Bean
    fun userLogin(userStorage: UserStorage): UserLogin = UserLoginImpl(userStorage)

    @Bean
    fun userSelection(
        userStorage: UserStorage,
        userEmailSelector: UserEmailSelector
    ): UserSelection = UserSelectionImpl(userStorage, userEmailSelector)
}
