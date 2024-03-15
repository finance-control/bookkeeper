package com.marsofandrew.bookkeeper.category.impl.spending

import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingCategoryValidationImplTest {

    private val spendingCategoryStorage = mockk<CategoryStorage<SpendingUserCategory>>()

    private lateinit var spendingCategoryValidationImpl: SpendingCategoryValidationImpl

    @BeforeEach
    fun setup() {
        spendingCategoryValidationImpl = SpendingCategoryValidationImpl(spendingCategoryStorage)
    }

    @Test
    fun `validate when provided category owned by provided user then returns true`() {
        val userId = 5.asId<User>()
        val categoryId = 564.asId<SpendingUserCategory>()

        every { spendingCategoryStorage.existsByUserIdAndCategoryId(userId, categoryId) } returns true

        spendingCategoryValidationImpl.validate(userId, categoryId) shouldBe true

        verify(exactly = 1) { spendingCategoryStorage.existsByUserIdAndCategoryId(userId, categoryId) }
    }

    @Test
    fun `validate when provided category not owned by provided user then returns false`() {
        val userId = 5.asId<User>()
        val categoryId = 564.asId<SpendingUserCategory>()

        every { spendingCategoryStorage.existsByUserIdAndCategoryId(userId, categoryId) } returns false

        spendingCategoryValidationImpl.validate(userId, categoryId) shouldBe false

        verify(exactly = 1) { spendingCategoryStorage.existsByUserIdAndCategoryId(userId, categoryId) }
    }
}
