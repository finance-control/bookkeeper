package com.marsofandrew.bookkeeper.auth.security.configuration

import com.marsofandrew.bookkeeper.auth.client.ClientIdProvider
import com.marsofandrew.bookkeeper.auth.impl.BasicUserAuthenticationImpl
import com.marsofandrew.bookkeeper.auth.impl.FakeUserAuthenticationImpl
import com.marsofandrew.bookkeeper.auth.impl.TokenUserAuthenticationImpl
import com.marsofandrew.bookkeeper.auth.ip.IpAddressProvider
import com.marsofandrew.bookkeeper.auth.provider.UserIdByCredentialsProvider
import com.marsofandrew.bookkeeper.auth.provider.UserIdByTokenProvider
import com.marsofandrew.bookkeeper.auth.security.provider.BasicAuthenticationProvider
import com.marsofandrew.bookkeeper.auth.security.provider.FakeAuthenticationProvider
import com.marsofandrew.bookkeeper.auth.security.provider.TokenAuthenticationProvider
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

    @Bean
    fun tokenAuthenticationProvider(
        userIdByTokenProvider: UserIdByTokenProvider,
        ipAddressProvider: IpAddressProvider,
        clientIdProvider: ClientIdProvider
    ): AuthenticationProvider = TokenAuthenticationProvider(
        TokenUserAuthenticationImpl(userIdByTokenProvider, ipAddressProvider, clientIdProvider),
    )
}
