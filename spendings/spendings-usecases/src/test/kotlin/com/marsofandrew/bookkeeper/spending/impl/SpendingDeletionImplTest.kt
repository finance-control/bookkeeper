package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingDeletion
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.fixture.spending
import com.marsofandrew.bookkeeper.spending.user.User
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingDeletionImplTest {

    private val spendingStorage = mockk<SpendingStorage>(relaxUnitFun = true)
    private val eventPublisher = mockk<EventPublisher>(relaxUnitFun = true)

    private lateinit var spendingDeletion: SpendingDeletion

    @BeforeEach
    fun setup() {
        spendingDeletion = SpendingDeletionImpl(spendingStorage, eventPublisher)
    }

    @Test
    fun `delete deletes spending by ids`() {
        val userId = 5.asId<User>()
        val ids = setOf<NumericId<Spending>>(
            55.asId(),
            45.asId(),
            35.asId(),
        )

        val additionalIds = setOf<NumericId<Spending>>(
            84.asId()
        )

        val spendings = ids.mapTo(HashSet()) { spending(it, userId) }

        every { spendingStorage.findAllByUserIdAndIds(userId, ids + additionalIds) } returns spendings

        spendingDeletion.delete(userId, ids + additionalIds)

        val capturedIds = slot<Collection<NumericId<Spending>>>()
        verify(exactly = 1) { spendingStorage.delete(capture(capturedIds)) }
        capturedIds.captured shouldContainExactlyInAnyOrder ids

        verify(exactly = 1) { eventPublisher.publish(spendings.map { it.toRollbackMoneyIsSendEvent() }) }
    }
}

private fun Spending.toRollbackMoneyIsSendEvent() = RollbackMoneyIsSpendEvent(
    userId = userId.value,
    date = date,
    money = AccountBondedMoney(money, fromAccount?.value),
    category = spendingCategoryId.value
)
