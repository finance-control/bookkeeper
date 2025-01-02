package com.marsofandrew.bookkeeper.transfers.impl.earning

import com.marsofandrew.bookkeeper.base.exception.DomainModelNotFoundException
import com.marsofandrew.bookkeeper.base.transaction.TestTransactionExecutor
import com.marsofandrew.bookkeeper.event.Event
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.transfers.exception.InvalidCategoryException
import com.marsofandrew.bookkeeper.transfers.fixtures.earning
import com.marsofandrew.bookkeeper.transfers.fixtures.earningUpdate
import com.marsofandrew.bookkeeper.transfers.impl.TestTransferCategorySelector
import com.marsofandrew.bookkeeper.transfers.impl.utils.toMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.transfers.impl.utils.toRollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.transfers.updateEarning
import com.marsofandrew.bookkeeper.transfers.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EarningModificationImplTest {

    private val transferStorage = mockk<TransferStorage>()
    private val eventPublisher = mockk<EventPublisher>()
    private val transferAccountValidator = mockk<TransferAccountValidator>()
    private val transferCategoryValidator = mockk<TransferCategoryValidator>()

    private lateinit var earningModificationImpl: EarningModificationImpl

    @BeforeEach
    fun setup() {
        earningModificationImpl = EarningModificationImpl(
            transferStorage = transferStorage,
            eventPublisher = eventPublisher,
            transferAccountValidator = transferAccountValidator,
            transferCategoryValidator = transferCategoryValidator,
            transactionExecutor = TestTransactionExecutor(),
            transferCategorySelector = TestTransferCategorySelector()
        )

        every { eventPublisher.publish(any<Event>()) } returns Unit
    }

    @Test
    fun `modify throws exception when earning is absent`() {
        every {
            transferStorage.findByIdAndUserIdOrThrow(
                DEFAULT_EARNING.id,
                USER_ID
            )
        } throws DomainModelNotFoundException(
            DEFAULT_EARNING.id
        )

        shouldThrowExactly<DomainModelNotFoundException> {
            earningModificationImpl.modify(USER_ID, earningUpdate(DEFAULT_EARNING.id))
        }
    }

    @Test
    fun `modify throws exception when category is absent in DB`() {
        every {
            transferStorage.findByIdAndUserIdOrThrow(
                DEFAULT_EARNING.id,
                USER_ID
            )
        } returns DEFAULT_EARNING

        val update = earningUpdate(DEFAULT_EARNING.id) {
            received = AccountMoney(
                money = PositiveMoney(Currency.EUR, 10, 1),
                accountId = "test".asId()
            )
        }

        every { transferCategoryValidator.validate(USER_ID, DEFAULT_EARNING.categoryId) } returns false
        every { transferAccountValidator.validate(USER_ID, update.received!!.accountId!!) } returns true

        shouldThrowExactly<InvalidCategoryException> {
            earningModificationImpl.modify(USER_ID, update)
        }

        verify { eventPublisher wasNot Called }
    }

    @Test
    fun `modify throws exception when account is provided and absent in DB`() {
        every {
            transferStorage.findByIdAndUserIdOrThrow(
                DEFAULT_EARNING.id,
                USER_ID
            )
        } returns DEFAULT_EARNING

        val update = earningUpdate(DEFAULT_EARNING.id) {
            received = AccountMoney(
                money = PositiveMoney(Currency.EUR, 10, 1),
                accountId = "test".asId()
            )
        }

        every { transferCategoryValidator.validate(USER_ID, DEFAULT_EARNING.categoryId) } returns true
        every { transferAccountValidator.validate(USER_ID, update.received!!.accountId!!) } returns false

        shouldThrowExactly<InvalidAccountException> {
            earningModificationImpl.modify(USER_ID, update)
        }
        verify { eventPublisher wasNot Called }
    }

    @Test
    fun `modify modifies earning when all necessary data is provided`() {
        every {
            transferStorage.findByIdAndUserIdOrThrow(
                DEFAULT_EARNING.id,
                USER_ID
            )
        } returns DEFAULT_EARNING

        val update = earningUpdate(DEFAULT_EARNING.id) {
            received = AccountMoney(
                money = PositiveMoney(Currency.EUR, 10, 1),
                accountId = "test".asId()
            )
            description = "tttt"
        }

        every { transferStorage.update(DEFAULT_EARNING.updateEarning(update)) } returns DEFAULT_EARNING.updateEarning(
            update
        )

        every { transferCategoryValidator.validate(USER_ID, DEFAULT_EARNING.categoryId) } returns true
        every { transferAccountValidator.validate(USER_ID, update.received!!.accountId!!) } returns true

        val result = earningModificationImpl.modify(USER_ID, update)

        result.transfer.description shouldBe update.description
        verify(exactly = 1) { eventPublisher.publish(DEFAULT_EARNING.toRollbackMoneyIsTransferredEvent()) }
        verify(exactly = 1) {
            eventPublisher.publish(
                DEFAULT_EARNING.updateEarning(update).toMoneyIsTransferredEvent()
            )
        }
    }

    private companion object {
        val USER_ID = 543.asId<User>()
        val DEFAULT_EARNING = earning(654.asId(), USER_ID) {
            description = "test description"
        }
    }
}