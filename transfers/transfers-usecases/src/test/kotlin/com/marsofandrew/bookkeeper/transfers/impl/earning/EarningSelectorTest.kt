package com.marsofandrew.bookkeeper.transfers.impl.earning

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.exception.InvalidDateIntervalException
import com.marsofandrew.bookkeeper.transfers.fixtures.earning
import com.marsofandrew.bookkeeper.transfers.impl.TestCategorySelector
import com.marsofandrew.bookkeeper.transfers.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class EarningSelectorTest {

    private val transferStorage = mockk<TransferStorage>()

    private lateinit var earningSelectionImpl: EarningSelectionImpl

    @BeforeEach
    fun setup() {
        earningSelectionImpl = EarningSelectionImpl(transferStorage, TestCategorySelector())
    }

    @Test
    fun `select returns all transfers by user when no dates are provided`() {
        val now = LocalDate.now()
        val date1 = now.minusDays(1)

        val earnings = listOf(
            earning(1.asId(), userId) { date = now },
            earning(2.asId(), userId) { date = date1 },
            earning(3.asId(), userId) { date = now },
        )

        every { transferStorage.findAllByUserId(userId) } returns earnings

        val result = earningSelectionImpl.select(userId)

        verify(exactly = 1) { transferStorage.findAllByUserId(userId) }
        result.map { it.transfer } shouldContainExactlyInAnyOrder earnings
    }

    @Test
    fun `select when start date is provided returns transfers from that date`() {
        val now = LocalDate.now()
        val date1 = now.minusDays(1)

        val earnings = listOf(
            earning(1.asId(), userId) { date = now },
            earning(2.asId(), userId) { date = date1 },
            earning(3.asId(), userId) { date = now },
        )

        every { transferStorage.findAllByUserIdBetween(userId, date1, now.plusDays(1)) } returns earnings

        val result = earningSelectionImpl.select(userId, date1)

        verify(exactly = 1) { transferStorage.findAllByUserIdBetween(userId, date1, now.plusDays(1)) }
        result.map { it.transfer } shouldContainExactlyInAnyOrder earnings
    }

    @Test
    fun `select when start date and end date are provided returns transfers between that dates`() {
        val now = LocalDate.now()
        val date1 = now.minusDays(1)

        val earnings = listOf(
            earning(1.asId(), userId) { date = now },
            earning(2.asId(), userId) { date = date1 },
            earning(3.asId(), userId) { date = now },
        )

        every { transferStorage.findAllByUserIdBetween(userId, date1, now) } returns earnings

        val result = earningSelectionImpl.select(userId, date1, now)

        verify(exactly = 1) { transferStorage.findAllByUserIdBetween(userId, date1, now) }
        result.map { it.transfer } shouldContainExactlyInAnyOrder earnings
    }

    @Test
    fun `select throws exception when invalid dates interval is provided`() {
        shouldThrowExactly<InvalidDateIntervalException> {
            earningSelectionImpl.select(
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