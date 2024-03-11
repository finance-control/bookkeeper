package com.marsofandrew.bookkeeper.auth.security.configuration

import com.marsofandrew.bookkeeper.auth.impl.BasicUserAuthenticationImpl
import com.marsofandrew.bookkeeper.auth.impl.FakeUserAuthenticationImpl
import com.marsofandrew.bookkeeper.auth.provider.UserIdByCredentialsProvider
import com.marsofandrew.bookkeeper.auth.security.provider.BasicAuthenticationProvider
import com.marsofandrew.bookkeeper.auth.security.provider.FakeAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationProvider

@Configuration
internal class ProviderConfiguration {

    @Profile("development")
    @Bean
    fun fakeAuthenticationProvider(): AuthenticationProvider = FakeAuthenticationProvider(FakeUserAuthenticationImpl())

    @Bean
    fun basicAuthenticationProvider(
        userIdByCredentialsProvider: UserIdByCredentialsProvider
    ): AuthenticationProvider = BasicAuthenticationProvider(BasicUserAuthenticationImpl(userIdByCredentialsProvider))
}