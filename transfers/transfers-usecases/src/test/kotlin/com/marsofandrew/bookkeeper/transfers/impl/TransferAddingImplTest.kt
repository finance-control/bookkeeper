package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferAddingImplTest {

    private val transferStorage = mockk<TransferStorage>()

    private lateinit var addingTransferImpl: TransferAddingImpl

    @BeforeEach
    fun setup() {
        addingTransferImpl = TransferAddingImpl(transferStorage)
    }

    @Test
    fun `add adds transfer when earning is provided`() {
        val transfer = Transfer(
            id = StringId.unidentified(),
            userId = 5.asId(),
            date = LocalDate.now(),
            send = null,
            received = PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)),
            comment = "",
            0.asId()
        )

        every { transferStorage.create(transfer) } returns transfer

        addingTransferImpl.add(transfer)

        verify(exactly = 1) { transferStorage.create(transfer) }
    }
}