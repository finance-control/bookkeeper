package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.account.AccountValidator
import com.marsofandrew.bookkeeper.spending.category.CategoryValidator
import com.marsofandrew.bookkeeper.spending.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.spending.exception.InvalidCategoryException
import com.marsofandrew.bookkeeper.spending.fixture.spending
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingAddingImplTest {

    private val spendingStorage = mockk<SpendingStorage>()
    private val categoryValidator = mockk<CategoryValidator>()
    private val accountValidator = mockk<AccountValidator>()

    private val eventPublisher = mockk<EventPublisher>(relaxUnitFun = true)

    private lateinit var addingSpendingImpl: SpendingAddingImpl

    @BeforeEach
    fun setup() {
        addingSpendingImpl = SpendingAddingImpl(spendingStorage, eventPublisher, categoryValidator, accountValidator)
    }

    @Test
    fun `add adds spending when spending is provided `() {
        val spending = spending(
            id = NumericId.unidentified(),
            userId = 5.asId(),
        )

        every { accountValidator.validate(spending.userId, any()) } returns true
        every { categoryValidator.validate(spending.userId, spending.spendingCategoryId) } returns true
        every { spendingStorage.create(spending) } returns spending

        addingSpendingImpl.add(spending)

        verify(exactly = 1) { spendingStorage.create(spending) }
        verify(exactly = 1) {
            eventPublisher.publish(
                MoneyIsSpendEvent(
                    spending.userId.value,
                    spending.date,
                    AccountBondedMoney(spending.money, spending.fromAccount?.value),
                    spending.spendingCategoryId.value
                )
            )
        }
    }

    @Test
    fun `add throws exception when category does not belong to user`() {
        val spending = spending(
            id = NumericId.unidentified(),
            userId = 5.asId(),
        )

        every { categoryValidator.validate(spending.userId, spending.spendingCategoryId) } returns false

        shouldThrowExactly<InvalidCategoryException> {
            addingSpendingImpl.add(spending)
        }
    }

    @Test
    fun `add throws exception when account does not belong to user`() {
        val spending = spending(
            id = NumericId.unidentified(),
            userId = 5.asId(),
        )

        every { categoryValidator.validate(spending.userId, spending.spendingCategoryId) } returns true
        every { accountValidator.validate(spending.userId, any()) } returns false

        shouldThrowExactly<InvalidAccountException> {
            addingSpendingImpl.add(spending)
        }
    }
}
