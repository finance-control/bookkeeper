package com.marsofandrew.bookkeeper.category.impl.transfer

import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.fixture.transferUserCategory
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferCategoryAddingImplTest {

    private val transferCategoryStorage = mockk<CategoryStorage<TransferUserCategory>>()

    private lateinit var transferCategoryAddingImpl: TransferCategoryAddingImpl

    @BeforeEach
    fun setup() {
        transferCategoryAddingImpl = TransferCategoryAddingImpl(transferCategoryStorage)
    }

    @Test
    fun `add when category is provided the creates category`() {
        val transferUserCategory = transferUserCategory(NumericId.unidentified())
        val identifiedTransferCategory = transferUserCategory.copy(id = 5.asId())

        every { transferCategoryStorage.create(transferUserCategory) } returns identifiedTransferCategory

        val result = transferCategoryAddingImpl.add(transferUserCategory)

        result shouldBe identifiedTransferCategory
        verify(exactly = 1) { transferCategoryStorage.create(transferUserCategory) }
    }
}
