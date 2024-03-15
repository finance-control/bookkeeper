package com.marsofandrew.bookkeeper.category.impl.spending

import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.fixture.spendingUserCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingCategoryDeletionImplTest {

    private val spendingCategoryStorage = mockk<CategoryStorage<SpendingUserCategory>>(relaxUnitFun = true)

    private lateinit var spendingCategoryDeletionImpl: SpendingCategoryDeletionImpl

    @BeforeEach
    fun setup() {
        spendingCategoryDeletionImpl = SpendingCategoryDeletionImpl(spendingCategoryStorage)
    }

    @Test
    fun `delete when categories ids not owned by user are provided then only categories owned by user are deleted`() {
        val ids: Set<NumericId<SpendingUserCategory>> = setOf(5.asId(), 7.asId(), 9.asId())
        val additionalIds: Set<NumericId<SpendingUserCategory>> = setOf(15.asId(), 17.asId(), 19.asId())

        val providedUserId = 45.asId<User>()

        val categories = ids.map { spendingUserCategory(it) { userId = providedUserId } }

        every { spendingCategoryStorage.findAllByUserIdAndIds(providedUserId, ids + additionalIds) } returns categories

        spendingCategoryDeletionImpl.delete(providedUserId, ids + additionalIds)

        verify(exactly = 1) { spendingCategoryStorage.delete(ids) }
    }
}
