package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferDeletionImplTest {

    private val transferStorage = mockk<TransferStorage>(relaxUnitFun = true)

    private lateinit var deletingTransfersImpl: TransferDeletionImpl

    @BeforeEach
    fun setup() {
        deletingTransfersImpl = TransferDeletionImpl(transferStorage)
    }

    @Test
    fun `delete deletes transfers by ids`() {
        val ids = setOf(StringId<Transfer>("test"));

        deletingTransfersImpl.delete(ids)

        verify(exactly = 1) { deletingTransfersImpl.delete(ids) }
    }
}