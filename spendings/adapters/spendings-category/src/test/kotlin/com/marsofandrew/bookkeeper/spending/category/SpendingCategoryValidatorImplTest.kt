package com.marsofandrew.bookkeeper.spending.category

import com.marsofandrew.bookkeeper.category.CategoryValidation
import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.fixture.spending
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingCategoryValidatorImplTest {

    private val categoryValidation = mockk<CategoryValidation<SpendingUserCategory>>()
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
                spending.spendingCategoryId.value.asId()
            )
        } returns true

        val result = categoryValidatorImpl.validate(spending.userId, spending.spendingCategoryId)

        result shouldBe true
    }

    @Test
    fun `validate when userCategory does not belong to userId returns false`() {
        val spending = spending(45.asId(), 15.asId())

        every {
            categoryValidation.validate(
                spending.userId.value.asId(),
                spending.spendingCategoryId.value.asId()
            )
        } returns false

        val result = categoryValidatorImpl.validate(spending.userId, spending.spendingCategoryId)

        result shouldBe false
    }
}