package com.marsofandrew.bookkeeper.transfer.impl

import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.AccountMoney
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfer.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfer.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.transfer.exception.InvalidCategoryException
import com.marsofandrew.bookkeeper.transfer.fixtures.transfer
import com.marsofandrew.bookkeeper.transfer.impl.utils.toAccountBoundedMoney
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.Called
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
    private val transferAccountValidator = mockk<TransferAccountValidator>()
    private val transferCategoryValidator = mockk<TransferCategoryValidator>()

    private lateinit var addingTransferImpl: TransferAddingImpl

    @BeforeEach
    fun setup() {
        addingTransferImpl = TransferAddingImpl(transferStorage, eventPublisher, transferAccountValidator, transferCategoryValidator)
    }

    @Test
    fun `add adds transfer when earning without accountId is provided`() {
        val transfer = transfer(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            send = null
            received = AccountMoney.create(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)))
            description = ""
            categoryId = 1.asId()
        }

        every { transferCategoryValidator.validate(transfer.userId, transfer.categoryId) } returns true

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
                    category = transfer.categoryId.value
                )
            )
        }

        verify(exactly = 1) { transferCategoryValidator.validate(transfer.userId, transfer.categoryId) }
        verify { transferAccountValidator wasNot Called }
    }

    @Test
    fun `add adds transfer when earning with accountId for received is provided`() {
        val transfer = transfer(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            send = null
            received = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc".asId())
            description = ""
            categoryId = 1.asId()
        }

        every { transferAccountValidator.validate(transfer.userId, transfer.received.accountId!!) } returns true
        every { transferCategoryValidator.validate(transfer.userId, transfer.categoryId) } returns true

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
                    category = transfer.categoryId.value
                )
            )
        }

        verify(exactly = 1) { transferCategoryValidator.validate(transfer.userId, transfer.categoryId) }
        verify(exactly = 1) { transferAccountValidator.validate(transfer.userId, transfer.received.accountId!!) }
    }

    @Test
    fun `add adds transfer when earning with accountId for received and send is provided`() {
        val transfer = transfer(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            send = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc2".asId())
            received = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc".asId())
            description = ""
            categoryId = 1.asId()
        }

        every { transferAccountValidator.validate(transfer.userId, transfer.received.accountId!!) } returns true
        every { transferAccountValidator.validate(transfer.userId, transfer.send!!.accountId!!) } returns true
        every { transferCategoryValidator.validate(transfer.userId, transfer.categoryId) } returns true

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
                    category = transfer.categoryId.value
                )
            )
        }

        verify(exactly = 1) { transferCategoryValidator.validate(transfer.userId, transfer.categoryId) }
        verify(exactly = 1) { transferAccountValidator.validate(transfer.userId, transfer.received.accountId!!) }
        verify(exactly = 1) { transferAccountValidator.validate(transfer.userId, transfer.send!!.accountId!!) }
    }

    @Test
    fun `add throws exception when provided earning with accountId for received does not belong to user`() {
        val transfer = transfer(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            send = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc2".asId())
            received = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc".asId())
            description = ""
            categoryId = 1.asId()
        }

        every { transferAccountValidator.validate(transfer.userId, transfer.received.accountId!!) } returns false
        every { transferAccountValidator.validate(transfer.userId, transfer.send!!.accountId!!) } returns true
        every { transferCategoryValidator.validate(transfer.userId, transfer.categoryId) } returns true

        shouldThrowExactly<InvalidAccountException> {
            addingTransferImpl.add(transfer)
        }

        verify { transferStorage wasNot Called }
        verify { eventPublisher wasNot Called }
    }

    @Test
    fun `add throws exception when provided earning with accountId for send does not belong to user`() {
        val transfer = transfer(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            send = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc2".asId())
            received = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc".asId())
            description = ""
            categoryId = 1.asId()
        }

        every { transferAccountValidator.validate(transfer.userId, transfer.received.accountId!!) } returns true
        every { transferAccountValidator.validate(transfer.userId, transfer.send!!.accountId!!) } returns false
        every { transferCategoryValidator.validate(transfer.userId, transfer.categoryId) } returns true

        shouldThrowExactly<InvalidAccountException> {
            addingTransferImpl.add(transfer)
        }

        verify { transferStorage wasNot Called }
        verify { eventPublisher wasNot Called }
    }

    @Test
    fun `add throws exception when provided earning has categoryId that does not belong to user`() {
        val transfer = transfer(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            send = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc2".asId())
            received = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc".asId())
            description = ""
            categoryId = 1.asId()
        }

        every { transferCategoryValidator.validate(transfer.userId, transfer.categoryId) } returns false

        shouldThrowExactly<InvalidCategoryException> {
            addingTransferImpl.add(transfer)
        }

        verify { transferStorage wasNot Called }
        verify { eventPublisher wasNot Called }
    }
}
