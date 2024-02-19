package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.fixtures.transfer
import com.marsofandrew.bookkeeper.transfers.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class TransferSelectionImplTest {

    private val transferStorage = mockk<TransferStorage>()

    private lateinit var selectingTransfersImpl: TransferSelectionImpl

    @BeforeEach
    fun setup() {
        selectingTransfersImpl = TransferSelectionImpl(transferStorage)
    }

    @Test
    fun `select returns all transfers by user when no dates are provided`() {
        val now = LocalDate.now()
        val date1 = now.minusDays(1)

        val transfers = listOf(
            transfer("1".asId(), userId) { date = now },
            transfer("2".asId(), userId) { date = date1 },
            transfer("3".asId(), userId) { date = now },
        )

        every { transferStorage.findAllByUserId(userId) } returns transfers

        val result = selectingTransfersImpl.select(userId)

        verify(exactly = 1) { transferStorage.findAllByUserId(userId) }
        result shouldContainExactlyInAnyOrder transfers
    }

    @Test
    fun `select when start date is provided returns transfers from that date`() {
        val now = LocalDate.now()
        val date1 = now.minusDays(1)

        val transfers = listOf(
            transfer("1".asId(), userId) { date = now },
            transfer("2".asId(), userId) { date = date1 },
            transfer("3".asId(), userId) { date = now },
        )

        every { transferStorage.findAllByUserIdBetween(userId, date1, now.plusDays(1)) } returns transfers

        val result = selectingTransfersImpl.select(userId, date1)

        verify(exactly = 1) { transferStorage.findAllByUserIdBetween(userId, date1, now.plusDays(1)) }
        result shouldContainExactlyInAnyOrder transfers
    }

    @Test
    fun `select when start date and end date are provided returns transfers from between that dates`() {
        val now = LocalDate.now()
        val date1 = now.minusDays(1)

        val transfers = listOf(
            transfer("1".asId(), userId) { date = now },
            transfer("2".asId(), userId) { date = date1 },
            transfer("3".asId(), userId) { date = now },
        )

        every { transferStorage.findAllByUserIdBetween(userId, date1, now) } returns transfers

        val result = selectingTransfersImpl.select(userId, date1, now)

        verify(exactly = 1) { transferStorage.findAllByUserIdBetween(userId, date1, now) }
        result shouldContainExactlyInAnyOrder transfers
    }

    @Test
    fun `select throws exception when invalid dates interval is provided`() {
        shouldThrowExactly<IllegalArgumentException> {
            selectingTransfersImpl.select(
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