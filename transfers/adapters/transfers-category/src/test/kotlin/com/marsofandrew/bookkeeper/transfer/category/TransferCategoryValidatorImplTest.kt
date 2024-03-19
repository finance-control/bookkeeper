package com.marsofandrew.bookkeeper.transfer.category

import com.marsofandrew.bookkeeper.category.CategoryValidation
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.fixtures.transfer
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferCategoryValidatorImplTest {

    private val categoryValidation = mockk<CategoryValidation<TransferUserCategory>>()
    private lateinit var categoryValidatorImpl: TransferCategoryValidatorImpl

    @BeforeEach
    fun setup() {
        categoryValidatorImpl = TransferCategoryValidatorImpl(categoryValidation)
    }

    @Test
    fun `validate when userCategory belongs to userId returns true`() {
        val transfer = transfer(45.asId(), 15.asId())

        every {
            categoryValidation.validate(
                transfer.userId.value.asId(),
                transfer.transferCategoryId.value.asId()
            )
        } returns true

        val result = categoryValidatorImpl.validate(transfer.userId, transfer.transferCategoryId)

        result shouldBe true
    }

    @Test
    fun `validate when userCategory does not belong to userId returns false`() {
        val transfer = transfer(45.asId(), 15.asId())

        every {
            categoryValidation.validate(
                transfer.userId.value.asId(),
                transfer.transferCategoryId.value.asId()
            )
        } returns false

        val result = categoryValidatorImpl.validate(transfer.userId, transfer.transferCategoryId)

        result shouldBe false
    }
}