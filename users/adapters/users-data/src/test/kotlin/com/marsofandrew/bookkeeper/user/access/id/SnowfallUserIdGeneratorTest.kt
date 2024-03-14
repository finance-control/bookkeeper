package com.marsofandrew.bookkeeper.user.access.id

import com.marsofandrew.bookkeeper.user.User
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Random
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SnowfallUserIdGeneratorTest {

    private val clock = Clock.fixed(
        LocalDateTime.of(2024, 3, 11, 0, 5, 0)
            .toInstant(ZoneOffset.of("Z")),
        ZoneId.of("Z")
    )

    private val randomGenerator = mockk<Random>()
    private val hostId: Byte = 1

    private lateinit var snowfallUserIdGenerator: SnowfallUserIdGenerator

    @BeforeEach
    fun setup() {
        snowfallUserIdGenerator = SnowfallUserIdGenerator(
            clock,
            randomGenerator,
            hostId
        )
    }

    @Test
    fun `generateId generates uniqueId`() {
        every { randomGenerator.nextInt(SnowfallUserIdGenerator.MAX_RANDOM) } returns 10
        val expectedId =
            (clock.instant().epochSecond - User.APP_EPOCH_SECONDS).shl(30) + 10 + hostId.toLong().shl(25)

        snowfallUserIdGenerator.generateId().value shouldBe expectedId
    }
}
