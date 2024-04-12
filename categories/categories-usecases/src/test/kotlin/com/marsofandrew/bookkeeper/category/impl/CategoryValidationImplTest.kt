package com.marsofandrew.bookkeeper.category.impl

import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CategoryValidationImplTest {

    private val categoryStorage = mockk<CategoryStorage>()

    private lateinit var categoryValidationImpl: CategoryValidationImpl

    @BeforeEach
    fun setup() {
        categoryValidationImpl = CategoryValidationImpl(categoryStorage)
    }

    @Test
    fun `validate when provided category owned by provided user then returns true`() {
        val userId = 5.asId<User>()
        val categoryId = 564.asId<UserCategory>()

        every { categoryStorage.existsByUserIdAndCategoryId(userId, categoryId) } returns true

        categoryValidationImpl.validate(userId, categoryId) shouldBe true

        verify(exactly = 1) { categoryStorage.existsByUserIdAndCategoryId(userId, categoryId) }
    }

    @Test
    fun `validate when provided category not owned by provided user then returns false`() {
        val userId = 5.asId<User>()
        val categoryId = 564.asId<UserCategory>()

        every { categoryStorage.existsByUserIdAndCategoryId(userId, categoryId) } returns false

        categoryValidationImpl.validate(userId, categoryId) shouldBe false

        verify(exactly = 1) { categoryStorage.existsByUserIdAndCategoryId(userId, categoryId) }
    }
}
