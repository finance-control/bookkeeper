package com.marsofandrew.bookkeeper.category.impl

import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.fixture.userCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CategorySelectionImplTest {

    private val categoryStorage = mockk<CategoryStorage>()

    private lateinit var categorySelectionImpl: CategorySelectionImpl

    @BeforeEach
    fun setup() {
        categorySelectionImpl = CategorySelectionImpl(categoryStorage)
    }

    @Test
    fun `select when categories by user id is present then returns that categories`() {
        val providedUserId = 487.asId<User>()
        val categories = listOf(
            userCategory(54.asId()) { userId = providedUserId },
            userCategory(57.asId()) { userId = providedUserId },
        )

        every { categoryStorage.findAllByUserId(providedUserId) } returns categories

        val result = categorySelectionImpl.select(providedUserId)

        result shouldBe categories
    }

    @Test
    fun `select when categories by user id is absent then returns that emptyList`() {
        val userId = 487.asId<User>()

        every { categoryStorage.findAllByUserId(userId) } returns emptyList()

        val result = categorySelectionImpl.select(userId)

        result shouldBe emptyList()
    }
}
