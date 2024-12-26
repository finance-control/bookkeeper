package com.marsofandrew.bookkeeper.tokens

import com.marsofandrew.bookkeeper.tokens.access.TokenStorage
import com.marsofandrew.bookkeeper.tokens.hash.HashGenerator
import com.marsofandrew.bookkeeper.tokens.impl.TokenCreationImpl
import com.marsofandrew.bookkeeper.tokens.impl.TokenExpirationImpl
import com.marsofandrew.bookkeeper.tokens.impl.TokenUserIdSelectionImpl
import java.time.Clock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class TokensContextConfiguration {

    @Bean
    fun tokenCreation(
        tokenStorage: TokenStorage,
        hashGenerator: HashGenerator,
        clock: Clock,
    ): TokenCreation = TokenCreationImpl(
        tokenStorage = tokenStorage,
        hashGenerator = hashGenerator,
        clock = clock,
    )

    @Bean
    fun tokenExpiration(
        tokenStorage: TokenStorage,
        clock: Clock
    ): TokenExpiration = TokenExpirationImpl(
        tokenStorage = tokenStorage,
        clock = clock
    )

    @Bean
    fun tokenUserIdSelection(
        tokenStorage: TokenStorage,
        clock: Clock
    ): TokenUserIdSelection = TokenUserIdSelectionImpl(
        tokenStorage = tokenStorage,
        clock = clock
    )

}
