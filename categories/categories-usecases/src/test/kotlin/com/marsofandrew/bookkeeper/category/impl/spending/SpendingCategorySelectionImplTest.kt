package com.marsofandrew.bookkeeper.category.impl.spending

import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.fixture.spendingUserCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingCategorySelectionImplTest {

    private val spendingCategoryStorage = mockk<CategoryStorage<SpendingUserCategory>>()

    private lateinit var spendingCategorySelectionImpl: SpendingCategorySelectionImpl

    @BeforeEach
    fun setup() {
        spendingCategorySelectionImpl = SpendingCategorySelectionImpl(spendingCategoryStorage)
    }

    @Test
    fun `select when categories by user id is present then returns that categories`() {
        val providedUserId = 487.asId<User>()
        val spendingCategories = listOf(
            spendingUserCategory(54.asId()) { userId = providedUserId },
            spendingUserCategory(57.asId()) { userId = providedUserId },
        )

        every { spendingCategoryStorage.findAllByUserId(providedUserId) } returns spendingCategories

        val result = spendingCategorySelectionImpl.select(providedUserId)

        result shouldBe spendingCategories
    }

    @Test
    fun `select when categories by user id is absent then returns that emptyList`() {
        val userId = 487.asId<User>()

        every { spendingCategoryStorage.findAllByUserId(userId) } returns emptyList()

        val result = spendingCategorySelectionImpl.select(userId)

        result shouldBe emptyList()
    }
}
