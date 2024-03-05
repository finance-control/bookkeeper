package com.marsofandrew.bookkeeper.transfer.impl

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferDeletionImplTest {

    private val transferStorage = mockk<TransferStorage>(relaxUnitFun = true)
    private val eventPublisher = mockk<EventPublisher>(relaxUnitFun = true)

    private lateinit var deletingTransfersImpl: TransferDeletionImpl

    @BeforeEach
    fun setup() {
        deletingTransfersImpl = TransferDeletionImpl(transferStorage, eventPublisher)
    }

    @Test
    fun `delete deletes transfers by ids`() {
        val ids = setOf(1.asId<Transfer>());

        deletingTransfersImpl.delete(1.asId(), ids)

        verify(exactly = 1) { transferStorage.delete(ids) }
    }
}