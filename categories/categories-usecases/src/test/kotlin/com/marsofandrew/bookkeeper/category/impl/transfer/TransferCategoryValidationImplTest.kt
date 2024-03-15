package com.marsofandrew.bookkeeper.category.impl.transfer

import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferCategoryValidationImplTest {

    private val transferCategoryStorage = mockk<CategoryStorage<TransferUserCategory>>()

    private lateinit var transferCategoryValidationImpl: TransferCategoryValidationImpl

    @BeforeEach
    fun setup() {
        transferCategoryValidationImpl = TransferCategoryValidationImpl(transferCategoryStorage)
    }

    @Test
    fun `validate when provided category owned by provided user then returns true`() {
        val userId = 5.asId<User>()
        val categoryId = 564.asId<TransferUserCategory>()

        every { transferCategoryStorage.existsByUserIdAndCategoryId(userId, categoryId) } returns true

        transferCategoryValidationImpl.validate(userId, categoryId) shouldBe true

        verify(exactly = 1) {  transferCategoryStorage.existsByUserIdAndCategoryId(userId, categoryId) }
    }

    @Test
    fun `validate when provided category not owned by provided user then returns false`() {
        val userId = 5.asId<User>()
        val categoryId = 564.asId<TransferUserCategory>()

        every { transferCategoryStorage.existsByUserIdAndCategoryId(userId, categoryId) } returns false

        transferCategoryValidationImpl.validate(userId, categoryId) shouldBe false

        verify(exactly = 1) {  transferCategoryStorage.existsByUserIdAndCategoryId(userId, categoryId) }
    }
}
