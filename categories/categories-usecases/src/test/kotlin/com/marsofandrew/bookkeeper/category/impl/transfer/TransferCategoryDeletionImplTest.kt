package com.marsofandrew.bookkeeper.category.impl.transfer

import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.fixture.transferUserCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferCategoryDeletionImplTest {

    private val transferCategoryStorage = mockk<CategoryStorage<TransferUserCategory>>(relaxUnitFun = true)

    private lateinit var transferCategoryDeletionImpl: TransferCategoryDeletionImpl

    @BeforeEach
    fun setup() {
        transferCategoryDeletionImpl = TransferCategoryDeletionImpl(transferCategoryStorage)
    }

    @Test
    fun `delete when categories ids not owned by user are provided then only categories owned by user are deleted`() {
        val ids: Set<NumericId<TransferUserCategory>> = setOf(5.asId(), 7.asId(), 9.asId())
        val additionalIds: Set<NumericId<TransferUserCategory>> = setOf(15.asId(), 17.asId(), 19.asId())

        val providedUserId = 45.asId<User>()

        val categories = ids.map { transferUserCategory(it) { userId = providedUserId } }

        every { transferCategoryStorage.findAllByUserIdAndIds(providedUserId, ids + additionalIds) } returns categories

        transferCategoryDeletionImpl.delete(providedUserId, ids + additionalIds)

        verify(exactly = 1) { transferCategoryStorage.delete(ids) }
    }
}
