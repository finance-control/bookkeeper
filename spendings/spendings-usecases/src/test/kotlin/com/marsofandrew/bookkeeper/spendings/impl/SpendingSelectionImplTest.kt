package com.marsofandrew.bookkeeper.spendings.impl

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spendings.access.SpendingStorage
import com.marsofandrew.bookkeeper.spendings.fixtures.spending
import com.marsofandrew.bookkeeper.spendings.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class SpendingSelectionImplTest {

    private val spendingStorage = mockk<SpendingStorage>()

    private lateinit var selectingSpendingImpl: SpendingSelectionImpl

    @BeforeEach
    fun setup() {
        selectingSpendingImpl = SpendingSelectionImpl(spendingStorage)
    }

    @Test
    fun `select returns all spendings by user when no dates are provided`() {
        val now = LocalDate.now()
        val date1 = now.minusDays(1)

        val spendings = listOf(
            spending("1".asId(), userId) { date = now },
            spending("2".asId(), userId) { date = date1 },
            spending("3".asId(), userId) { date = now },
        )

        every { spendingStorage.findAllByUserId(userId) } returns spendings

        val result = selectingSpendingImpl.select(userId)

        verify(exactly = 1) { spendingStorage.findAllByUserId(userId) }
        result shouldContainExactlyInAnyOrder spendings
    }

    @Test
    fun `select when start date is provided returns spendings from that date`() {
        val now = LocalDate.now()
        val date1 = now.minusDays(1)

        val spendings = listOf(
            spending("1".asId(), userId) { date = now },
            spending("2".asId(), userId) { date = date1 },
            spending("3".asId(), userId) { date = now },
        )

        every { spendingStorage.findAllByUserIdBetween(userId, date1, now.plusDays(1)) } returns spendings

        val result = selectingSpendingImpl.select(userId, date1)

        verify(exactly = 1) { spendingStorage.findAllByUserIdBetween(userId, date1, now.plusDays(1)) }
        result shouldContainExactlyInAnyOrder spendings
    }

    @Test
    fun `select when start date and end date are provided returns spendings from between that dates`() {
        val now = LocalDate.now()
        val date1 = now.minusDays(1)

        val spendings = listOf(
            spending("1".asId(), userId) { date = now },
            spending("2".asId(), userId) { date = date1 },
            spending("3".asId(), userId) { date = now },
        )

        every { spendingStorage.findAllByUserIdBetween(userId, date1, now) } returns spendings

        val result = selectingSpendingImpl.select(userId, date1, now)

        verify(exactly = 1) { spendingStorage.findAllByUserIdBetween(userId, date1, now) }
        result shouldContainExactlyInAnyOrder spendings
    }

    @Test
    fun `select throws exception when invalid dates interval is provided`() {
        shouldThrowExactly<IllegalArgumentException> {
            selectingSpendingImpl.select(
                userId,
                LocalDate.now(),
                LocalDate.now().minusDays(1)
            )
        }
    }

    private companion object {
        val userId = 5.asId<User>()
    }
}