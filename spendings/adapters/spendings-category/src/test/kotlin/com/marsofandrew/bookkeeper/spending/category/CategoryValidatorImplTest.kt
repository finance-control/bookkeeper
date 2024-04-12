package com.marsofandrew.bookkeeper.spending.category

import com.marsofandrew.bookkeeper.category.CategoryValidation
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.fixture.spending
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CategoryValidatorImplTest {

    private val categoryValidation = mockk<CategoryValidation>()
    private lateinit var categoryValidatorImpl: SpendingCategoryValidatorImpl

    @BeforeEach
    fun setup() {
        categoryValidatorImpl = SpendingCategoryValidatorImpl(categoryValidation)
    }

    @Test
    fun `validate when userCategory belongs to userId returns true`() {
        val spending = spending(45.asId(), 15.asId())

        every {
            categoryValidation.validate(
                spending.userId.value.asId(),
                spending.categoryId.value.asId()
            )
        } returns true

        val result = categoryValidatorImpl.validate(spending.userId, spending.categoryId)

        result shouldBe true
    }

    @Test
    fun `validate when userCategory does not belong to userId returns false`() {
        val spending = spending(45.asId(), 15.asId())

        every {
            categoryValidation.validate(
                spending.userId.value.asId(),
                spending.categoryId.value.asId()
            )
        } returns false

        val result = categoryValidatorImpl.validate(spending.userId, spending.categoryId)

        result shouldBe false
    }
}