package com.marsofandrew.bookkeeper.category.impl

import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.fixture.userCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CategoryDeletionImplTest {

    private val categoryStorage = mockk<CategoryStorage>(relaxUnitFun = true)

    private lateinit var categoryDeletionImpl: CategoryDeletionImpl

    @BeforeEach
    fun setup() {
        categoryDeletionImpl = CategoryDeletionImpl(categoryStorage)
    }

    @Test
    fun `delete when categories ids not owned by user are provided then only categories owned by user are deleted`() {
        val ids: Set<NumericId<UserCategory>> = setOf(5.asId(), 7.asId(), 9.asId())
        val additionalIds: Set<NumericId<UserCategory>> = setOf(15.asId(), 17.asId(), 19.asId())

        val providedUserId = 45.asId<User>()

        val categories = ids.map { userCategory(it) { userId = providedUserId } }

        every { categoryStorage.findAllByUserIdAndIds(providedUserId, ids + additionalIds) } returns categories

        categoryDeletionImpl.delete(providedUserId, ids + additionalIds)

        verify(exactly = 1) { categoryStorage.delete(ids) }
    }
}
