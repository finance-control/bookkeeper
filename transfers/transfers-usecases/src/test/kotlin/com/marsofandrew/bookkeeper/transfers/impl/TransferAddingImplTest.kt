package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.events.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.events.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.fixtures.transfer
import com.marsofandrew.bookkeeper.transfers.impl.utils.toAccountBoundedMoney
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
    private val eventPublisher = mockk<EventPublisher>(relaxUnitFun = true)

    private lateinit var addingTransferImpl: TransferAddingImpl

    @BeforeEach
    fun setup() {
        addingTransferImpl = TransferAddingImpl(transferStorage, eventPublisher)
    }

    @Test
    fun `add adds transfer when earning is provided`() {
        val transfer = transfer(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            send = null
            received = AccountMoney.create(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)))
            comment = ""
            transferCategoryId = 1.asId()
        }

        every { transferStorage.create(transfer) } returns transfer

        addingTransferImpl.add(transfer)

        verify(exactly = 1) { transferStorage.create(transfer) }
        verify(exactly = 1) {
            eventPublisher.publish(
                MoneyIsTransferredEvent(
                    userId = transfer.userId.value,
                    date = transfer.date,
                    send = transfer.send?.toAccountBoundedMoney(),
                    received = transfer.received.toAccountBoundedMoney(),
                    category = transfer.transferCategoryId.value
                )
            )
        }
    }
}