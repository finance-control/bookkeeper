package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.base.transaction.TestTransactionExecutor
import com.marsofandrew.bookkeeper.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.account.SpendingAccountValidator
import com.marsofandrew.bookkeeper.spending.category.SpendingCategoryValidator
import com.marsofandrew.bookkeeper.spending.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.spending.exception.InvalidCategoryException
import com.marsofandrew.bookkeeper.spending.fixture.spending
import com.marsofandrew.bookkeeper.spending.fixture.spendingUpdate
import com.marsofandrew.bookkeeper.spending.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class SpendingModificationImplTest {

    private val spendingStorage = mockk<SpendingStorage>()
    private val spendingCategoryValidator = mockk<SpendingCategoryValidator>()
    private val spendingAccountValidator = mockk<SpendingAccountValidator>()

    private val eventPublisher = mockk<EventPublisher>(relaxUnitFun = true)

    private lateinit var spendingModificationImpl: SpendingModificationImpl

    @BeforeEach
    fun setup() {
        spendingModificationImpl = SpendingModificationImpl(
            spendingStorage,
            eventPublisher,
            TestTransactionExecutor(),
            spendingCategoryValidator,
            spendingAccountValidator
        )

        every {
            spendingStorage.findByIdAndUserIdOrThrow(
                ORIGINAL_SPENDING.id,
                ORIGINAL_SPENDING.userId
            )
        } returns ORIGINAL_SPENDING
    }

    @Test
    fun `modify modifies spending when SpendingUpdate is provided `() {
        val update = spendingUpdate(
            id = ORIGINAL_SPENDING.id,
        ) {
            description = "new description"
            date = LocalDate.of(2023, 1, 1)
            money = PositiveMoney(Currency.RUB, 543, 1)
        }

        val expected = ORIGINAL_SPENDING.copy(
            description = update.description!!,
            date = update.date!!,
            money = update.money!!
        )

        every { spendingAccountValidator.validate(USER_ID, ORIGINAL_SPENDING.sourceAccountId!!) } returns true
        every { spendingCategoryValidator.validate(USER_ID, ORIGINAL_SPENDING.categoryId) } returns true
        every { spendingStorage.update(expected) } returns ORIGINAL_SPENDING

        spendingModificationImpl.modify(USER_ID, update)

        verify(exactly = 1) { spendingStorage.update(expected) }
        verify(exactly = 1) {
            eventPublisher.publish(
                MoneyIsSpendEvent(
                    expected.userId.value,
                    expected.date,
                    AccountBondedMoney(expected.money, expected.sourceAccountId?.value),
                    expected.categoryId.value
                )
            )
        }

        verify(exactly = 1) {
            eventPublisher.publish(
                RollbackMoneyIsSpendEvent(
                    ORIGINAL_SPENDING.userId.value,
                    ORIGINAL_SPENDING.date,
                    AccountBondedMoney(ORIGINAL_SPENDING.money, ORIGINAL_SPENDING.sourceAccountId?.value),
                    ORIGINAL_SPENDING.categoryId.value
                )
            )
        }
    }

    @Test
    fun `modify throws exception when category does not belong to user`() {
        val update = spendingUpdate(
            id = ORIGINAL_SPENDING.id
        ) {
            categoryId = 34.asId()
        }

        every { spendingCategoryValidator.validate(USER_ID, update.categoryId!!) } returns false

        shouldThrowExactly<InvalidCategoryException> {
            spendingModificationImpl.modify(USER_ID, update)
        }
    }

    @Test
    fun `modify throws exception when account does not belong to user`() {
        val update = spendingUpdate(
            id = ORIGINAL_SPENDING.id
        ) {
            sourceAccountId = "hh".asId()
            categoryId = 34.asId()
        }

        every { spendingCategoryValidator.validate(USER_ID, update.categoryId!!) } returns true
        every { spendingAccountValidator.validate(USER_ID, any()) } returns false

        shouldThrowExactly<InvalidAccountException> {
            spendingModificationImpl.modify(USER_ID, update)
        }
    }

    private companion object {
        val USER_ID = 5.asId<User>()
        val ORIGINAL_SPENDING = spending(4.asId(), USER_ID) {
            sourceAccountId = "test".asId()
        }
    }

}