package com.marsofandrew.bookkeeper.category.impl

import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.fixture.userCategory
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CategoryAddingImplTest {

    private val categoryStorage = mockk<CategoryStorage>()

    private lateinit var categoryAddingImpl: CategoryAddingImpl

    @BeforeEach
    fun setup() {
        categoryAddingImpl = CategoryAddingImpl(categoryStorage)
    }

    @Test
    fun `add when category is provided the creates category`() {
        val userCategory = userCategory(NumericId.unidentified())
        val identifiedUserCategory = userCategory.copy(id = 5.asId())

        every { categoryStorage.create(userCategory) } returns identifiedUserCategory

        val result = categoryAddingImpl.add(userCategory)

        result shouldBe identifiedUserCategory
        verify(exactly = 1) { categoryStorage.create(userCategory) }
    }
}
