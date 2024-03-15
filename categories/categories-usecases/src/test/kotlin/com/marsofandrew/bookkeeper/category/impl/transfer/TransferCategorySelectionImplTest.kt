package com.marsofandrew.bookkeeper.category.impl.transfer

import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.fixture.transferUserCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferCategorySelectionImplTest {

    private val transferCategoryStorage = mockk<CategoryStorage<TransferUserCategory>>()

    private lateinit var transferCategorySelectionImpl: TransferCategorySelectionImpl

    @BeforeEach
    fun setup() {
        transferCategorySelectionImpl = TransferCategorySelectionImpl(transferCategoryStorage)
    }

    @Test
    fun `select when categories by user id is present then returns that categories`() {
        val providedUserId = 487.asId<User>()
        val transferCategories = listOf(
            transferUserCategory(54.asId()) { userId = providedUserId },
            transferUserCategory(57.asId()) { userId = providedUserId },
        )

        every { transferCategoryStorage.findAllByUserId(providedUserId) } returns transferCategories

        val result = transferCategorySelectionImpl.select(providedUserId)

        result shouldBe transferCategories
    }

    @Test
    fun `select when categories by user id is absent then returns that emptyList`() {
        val userId = 487.asId<User>()

        every { transferCategoryStorage.findAllByUserId(userId) } returns emptyList()

        val result = transferCategorySelectionImpl.select(userId)

        result shouldBe emptyList()
    }
}
