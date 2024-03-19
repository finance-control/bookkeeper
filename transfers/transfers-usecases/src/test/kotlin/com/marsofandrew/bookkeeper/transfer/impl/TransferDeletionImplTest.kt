package com.marsofandrew.bookkeeper.transfer.impl

import com.marsofandrew.bookkeeper.event.RollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.fixtures.transfer
import com.marsofandrew.bookkeeper.transfer.impl.utils.toAccountBoundedMoney
import com.marsofandrew.bookkeeper.transfer.user.User
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferDeletionImplTest {

    private val transferStorage = mockk<TransferStorage>(relaxUnitFun = true)
    private val eventPublisher = mockk<EventPublisher>(relaxUnitFun = true)

    private lateinit var transferDeletionImpl: TransferDeletionImpl

    @BeforeEach
    fun setup() {
        transferDeletionImpl = TransferDeletionImpl(transferStorage, eventPublisher)
    }

    @Test
    fun `delete deletes transfers by ids`() {
        val userId = 5.asId<User>()
        val ids = setOf<NumericId<Transfer>>(
            55.asId(),
            45.asId(),
            35.asId(),
        )

        val additionalIds = setOf<NumericId<Transfer>>(
            84.asId()
        )

        val transfers = ids.mapTo(HashSet()) { transfer(it, userId) }

        every { transferStorage.findAllByUserIdAndIds(userId, ids + additionalIds) } returns transfers

        transferDeletionImpl.delete(userId, ids + additionalIds)

        val capturedIds = slot<Collection<NumericId<Transfer>>>()
        verify(exactly = 1) { transferStorage.delete(capture(capturedIds)) }

        capturedIds.captured shouldContainExactlyInAnyOrder ids

        verify(exactly = 1) { eventPublisher.publish(transfers.map { it.toRollbackMoneyIsTransferredEvent() }) }
    }
}

private fun Transfer.toRollbackMoneyIsTransferredEvent() = RollbackMoneyIsTransferredEvent(
    userId = userId.value,
    date = date,
    send = send?.toAccountBoundedMoney(),
    received = received.toAccountBoundedMoney(),
    category = transferCategoryId.value
)
