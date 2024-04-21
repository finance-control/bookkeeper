package com.marsofandrew.bookkeeper.transfers.impl.earning

import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.transfers.exception.InvalidCategoryException
import com.marsofandrew.bookkeeper.transfers.fixtures.earning
import com.marsofandrew.bookkeeper.transfers.impl.utils.toAccountBoundedMoney
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

internal class EarningAddingImplTest {

    private val transferStorage = mockk<TransferStorage>()
    private val eventPublisher = mockk<EventPublisher>(relaxUnitFun = true)
    private val transferAccountValidator = mockk<TransferAccountValidator>()
    private val transferCategoryValidator = mockk<TransferCategoryValidator>()

    private lateinit var earningAddingImpl: EarningAddingImpl

    @BeforeEach
    fun setup() {
        earningAddingImpl =
            EarningAddingImpl(transferStorage, eventPublisher, transferAccountValidator, transferCategoryValidator)
    }

    @Test
    fun `add adds transfer when earning without accountId is provided`() {
        val earning = earning(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            received = AccountMoney.create(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)))
            description = ""
            categoryId = 1.asId()
        }

        every { transferCategoryValidator.validate(earning.userId, earning.categoryId) } returns true

        every { transferStorage.create(earning) } returns earning

        earningAddingImpl.add(earning)

        verify(exactly = 1) { transferStorage.create(earning) }
        verify(exactly = 1) {
            eventPublisher.publish(
                MoneyIsTransferredEvent(
                    userId = earning.userId.value,
                    date = earning.date,
                    send = earning.send?.toAccountBoundedMoney(),
                    received = earning.received.toAccountBoundedMoney(),
                    category = earning.categoryId.value
                )
            )
        }

        verify(exactly = 1) { transferCategoryValidator.validate(earning.userId, earning.categoryId) }
        verify { transferAccountValidator wasNot Called }
    }

    @Test
    fun `add adds transfer when earning with accountId for received is provided`() {
        val earning = earning(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            received = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc".asId())
            description = ""
            categoryId = 1.asId()
        }

        every { transferAccountValidator.validate(earning.userId, earning.received.accountId!!) } returns true
        every { transferCategoryValidator.validate(earning.userId, earning.categoryId) } returns true

        every { transferStorage.create(earning) } returns earning

        earningAddingImpl.add(earning)

        verify(exactly = 1) { transferStorage.create(earning) }
        verify(exactly = 1) {
            eventPublisher.publish(
                MoneyIsTransferredEvent(
                    userId = earning.userId.value,
                    date = earning.date,
                    send = earning.send?.toAccountBoundedMoney(),
                    received = earning.received.toAccountBoundedMoney(),
                    category = earning.categoryId.value
                )
            )
        }

        verify(exactly = 1) { transferCategoryValidator.validate(earning.userId, earning.categoryId) }
        verify(exactly = 1) { transferAccountValidator.validate(earning.userId, earning.received.accountId!!) }
    }

    @Test
    fun `add throws exception when provided earning with accountId for received does not belong to user`() {
        val earning = earning(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            received = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc".asId())
            description = ""
            categoryId = 1.asId()
        }

        every { transferAccountValidator.validate(earning.userId, earning.received.accountId!!) } returns false
        every { transferCategoryValidator.validate(earning.userId, earning.categoryId) } returns true

        shouldThrowExactly<InvalidAccountException> {
            earningAddingImpl.add(earning)
        }

        verify { transferStorage wasNot Called }
        verify { eventPublisher wasNot Called }
    }

    @Test
    fun `add throws exception when provided earning has categoryId that does not belong to user`() {
        val earning = earning(
            id = NumericId.unidentified(),
            userId = 5.asId()
        ) {
            date = LocalDate.now()
            received = AccountMoney(PositiveMoney(Currency.EUR, BigDecimal.valueOf(10)), "acc".asId())
            description = ""
            categoryId = 1.asId()
        }

        every { transferCategoryValidator.validate(earning.userId, earning.categoryId) } returns false

        shouldThrowExactly<InvalidCategoryException> {
            earningAddingImpl.add(earning)
        }

        verify { transferStorage wasNot Called }
        verify { eventPublisher wasNot Called }
    }
}
