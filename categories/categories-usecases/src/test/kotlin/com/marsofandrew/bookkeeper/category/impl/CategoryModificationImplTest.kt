package com.marsofandrew.bookkeeper.category.impl

import com.marsofandrew.bookkeeper.base.exception.DomainModelNotFoundException
import com.marsofandrew.bookkeeper.base.transaction.TestTransactionExecutor
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.fixture.userCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CategoryModificationImplTest {

    private val categoryStorage = mockk<CategoryStorage>(relaxUnitFun = true)
    private val transactionExecutor: TransactionExecutor = TestTransactionExecutor()

    private lateinit var categoryModificationImpl: CategoryModificationImpl

    @BeforeEach
    fun setup() {
        categoryModificationImpl = CategoryModificationImpl(categoryStorage, transactionExecutor)
    }

    @Test
    fun `modify throws exception when category doesn't exist`() {
        val category = userCategory(3.asId())

        every {
            categoryStorage.findByUserIdAndIdOrThrow(
                category.userId,
                category.id
            )
        } throws DomainModelNotFoundException(category.id)

        shouldThrowExactly<DomainModelNotFoundException> {
            categoryModificationImpl.modify(category.userId, category)
        }
    }

    @Test
    fun `modify throws exception when category user id and request user id are different`() {
        val categoryUserId = 1.asId<User>()
        val category = userCategory(3.asId()) {
            userId = categoryUserId
        }

        shouldThrowExactly<RuntimeException> {
            categoryModificationImpl.modify(5.asId(), category)
        }

        verify { categoryStorage wasNot Called }
    }

    @Test
    fun `modify when category is present updates the category`() {
        val category = userCategory(3.asId())

        every { categoryStorage.findByUserIdAndIdOrThrow(category.userId, category.id) } returns category
        every { categoryStorage.update(category) } returns category

        val result = categoryModificationImpl.modify(category.userId, category)

        result shouldBe category
        verify(exactly = 1) { categoryStorage.update(category) }
    }
}