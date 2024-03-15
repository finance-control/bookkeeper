package com.marsofandrew.bookkeeper.category.impl.spending

import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.fixture.spendingUserCategory
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingCategoryAddingImplTest {

    private val spendingCategoryStorage = mockk<CategoryStorage<SpendingUserCategory>>()

    private lateinit var spendingCategoryAddingImpl: SpendingCategoryAddingImpl

    @BeforeEach
    fun setup() {
        spendingCategoryAddingImpl = SpendingCategoryAddingImpl(spendingCategoryStorage)
    }

    @Test
    fun `add when category is provided the creates category`() {
        val spendingUserCategory = spendingUserCategory(NumericId.unidentified())
        val identifiedSpendingCategory = spendingUserCategory.copy(id = 5.asId())

        every { spendingCategoryStorage.create(spendingUserCategory) } returns identifiedSpendingCategory

        val result = spendingCategoryAddingImpl.add(spendingUserCategory)

        result shouldBe identifiedSpendingCategory
        verify(exactly = 1) { spendingCategoryStorage.create(spendingUserCategory) }
    }
}
