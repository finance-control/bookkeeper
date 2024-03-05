package com.marsofandrew.bookkeeper.transfer.impl

import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.AccountMoney
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.fixtures.transfer
import com.marsofandrew.bookkeeper.transfer.impl.utils.toAccountBoundedMoney
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import java.math.BigDecimal
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
            description = ""
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