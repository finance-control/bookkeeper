package com.marsofandrew.bookkeeper.category

import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class TransferUserCategoryTest {

    @ParameterizedTest
    @ValueSource(strings = ["", "\t", "\n", "  "])
    fun `constructor throws exception when title is blank`(title: String) {
        shouldThrowExactly<ObjectCreateValidationException> {
            TransferUserCategory(
                id = 5.asId(),
                userId = 1.asId(),
                title = title,
                description = null,
                version = Version(0)
            )
        }
    }

    @Test
    fun `constructor creates object when correct values are set`() {
        val id = 5
        val userId = 145
        val title = "title"
        val description = "description"

        val transfer = TransferUserCategory(
            id = id.asId(),
            userId = userId.asId(),
            title = title,
            description = description,
            version = Version(0)
        )

        transfer.id.value shouldBe id
        transfer.userId.value shouldBe userId
        transfer.title shouldBe title
        transfer.description shouldBe description
    }
}
