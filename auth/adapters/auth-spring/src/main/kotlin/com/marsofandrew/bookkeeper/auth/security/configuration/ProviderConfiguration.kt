package com.marsofandrew.bookkeeper.auth.security.configuration

import com.marsofandrew.bookkeeper.auth.impl.FakeUserAuthenticationImpl
import com.marsofandrew.bookkeeper.auth.security.provider.FakeAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class ProviderConfiguration {

    @Bean
    fun fakeAuthenticationProvider() = FakeAuthenticationProvider(FakeUserAuthenticationImpl())
}