package com.marsofandrew.bookkeeper.user.access.id

import java.time.Clock
import java.util.Random
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.random.asKotlinRandom

@Configuration
internal class IdGeneratorConfiguration {

    @Bean
    fun snowfallUserIdGenerator(
        clock: Clock,
        @Value("\${app.host.id}") hostId: Byte
    ): UserIdGenerator = SnowfallUserIdGenerator(
        clock,
        Random().asKotlinRandom(),
        hostId = hostId
    )
}