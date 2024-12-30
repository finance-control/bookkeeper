package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class UserTest {

    @Test
    fun `constructor when all values are correct creates User`() {
        val createdAt = Instant.now()
        val id = 1.asId<User>()
        val version = Version(0)
        val name = " my name"

        val result = User(
            id = id,
            version = version,
            name = name,
            createdAt = createdAt,
            updatedAt = createdAt
        )

        result.id shouldBe id
        result.version shouldBe version
        result.name shouldBe name
        result.createdAt shouldBe createdAt
        result.updatedAt shouldBe createdAt
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "\t", "\n"])
    fun `constructor throws exception when name is blank`(name: String) {
        val now = Instant.now()
        shouldThrowExactly<ObjectCreateValidationException> {
            User(
                id = 1.asId(),
                version = Version(0),
                name = name,
                createdAt = now,
                updatedAt = now
            )
        }
    }

    @Test
    fun `constructor throws exception when createdAt less than 2024-03-10 00-00-00Z`() {
        val now = Instant.now()
        val createdAt =
            LocalDateTime.of(2024, 3, 9, 23, 59, 59).toInstant(ZoneOffset.of("Z"))
        shouldThrowExactly<ObjectCreateValidationException> {
            User(
                id = 1.asId(),
                version = Version(0),
                name = "name",
                createdAt = createdAt,
                updatedAt = now
            )
        }
    }

    @Test
    fun `constructor throws exception when updatedAt is less than createdAt`() {
        val now = Instant.now()
        shouldThrowExactly<ObjectCreateValidationException> {
            User(
                id = 1.asId(),
                version = Version(0),
                name = "name",
                createdAt = now,
                updatedAt = now.minusSeconds(10)
            )
        }
    }
}