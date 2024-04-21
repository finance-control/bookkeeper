package com.marsofandrew.bookkeeper.transfers.impl.commonTransfer

import com.marsofandrew.bookkeeper.event.RollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.fixtures.commonTransfer
import com.marsofandrew.bookkeeper.transfers.impl.utils.toAccountBoundedMoney
import com.marsofandrew.bookkeeper.transfers.user.User
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CommonTransferDeletionImplTest {

    private val transferStorage = mockk<TransferStorage>(relaxUnitFun = true)
    private val eventPublisher = mockk<EventPublisher>(relaxUnitFun = true)

    private lateinit var transferDeletionImpl: CommonTransferDeletionImpl

    @BeforeEach
    fun setup() {
        transferDeletionImpl = CommonTransferDeletionImpl(transferStorage, eventPublisher)
    }

    @Test
    fun `delete deletes transfers by ids`() {
        val userId = 5.asId<User>()
        val ids = setOf<NumericId<CommonTransfer>>(
            55.asId(),
            45.asId(),
            35.asId(),
        )

        val additionalIds = setOf<NumericId<CommonTransfer>>(
            84.asId()
        )

        val transfers = ids.mapTo(HashSet()) { commonTransfer(it, userId) }

        every { transferStorage.findAllByUserIdAndIds(userId, ids + additionalIds) } returns transfers

        transferDeletionImpl.delete(userId, ids + additionalIds)

        val capturedIds = slot<Collection<NumericId<CommonTransfer>>>()
        verify(exactly = 1) { transferStorage.delete(capture(capturedIds)) }

        capturedIds.captured shouldContainExactlyInAnyOrder ids

        verify(exactly = 1) { eventPublisher.publish(transfers.map { it.toRollbackMoneyIsTransferredEvent() }) }
    }
}

private fun CommonTransfer.toRollbackMoneyIsTransferredEvent() = RollbackMoneyIsTransferredEvent(
    userId = userId.value,
    date = date,
    send = send?.toAccountBoundedMoney(),
    received = received.toAccountBoundedMoney(),
    category = categoryId.value
)
