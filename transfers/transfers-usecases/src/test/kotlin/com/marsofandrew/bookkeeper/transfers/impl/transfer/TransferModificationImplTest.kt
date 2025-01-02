package com.marsofandrew.bookkeeper.transfers.impl.transfer

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
import com.marsofandrew.bookkeeper.transfers.fixtures.transfer
import com.marsofandrew.bookkeeper.transfers.fixtures.transferUpdate
import com.marsofandrew.bookkeeper.transfers.impl.TestCategorySelector
import com.marsofandrew.bookkeeper.transfers.impl.utils.toMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.transfers.impl.utils.toRollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.transfers.updateTransfer
import com.marsofandrew.bookkeeper.transfers.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferModificationImplTest {

    private val transferStorage = mockk<TransferStorage>()
    private val eventPublisher = mockk<EventPublisher>()
    private val transferAccountValidator = mockk<TransferAccountValidator>()
    private val transferCategoryValidator = mockk<TransferCategoryValidator>()

    private lateinit var transferModificationImpl: TransferModificationImpl

    @BeforeEach
    fun setup() {
        transferModificationImpl = TransferModificationImpl(
            transferStorage = transferStorage,
            eventPublisher = eventPublisher,
            transferAccountValidator = transferAccountValidator,
            transferCategoryValidator = transferCategoryValidator,
            transactionExecutor = TestTransactionExecutor(),
            categorySelector = TestCategorySelector()
        )

        every { eventPublisher.publish(any<Event>()) } returns Unit
    }

    @Test
    fun `modify throws exception when transfer is absent`() {
        every {
            transferStorage.findByIdAndUserIdOrThrow(
                DEFAULT_TRANSFER.id,
                USER_ID
            )
        } throws DomainModelNotFoundException(
            DEFAULT_TRANSFER.id
        )

        shouldThrowExactly<DomainModelNotFoundException> {
            transferModificationImpl.modify(USER_ID, transferUpdate(DEFAULT_TRANSFER.id))
        }
    }

    @Test
    fun `modify throws exception when category is absent in DB`() {
        every {
            transferStorage.findByIdAndUserIdOrThrow(
                DEFAULT_TRANSFER.id,
                USER_ID
            )
        } returns DEFAULT_TRANSFER

        val update = transferUpdate(DEFAULT_TRANSFER.id) {
            received = AccountMoney(
                money = PositiveMoney(Currency.EUR, 10, 1),
                accountId = "test".asId()
            )
        }

        every { transferCategoryValidator.validate(USER_ID, DEFAULT_TRANSFER.categoryId) } returns false
        every { transferAccountValidator.validate(USER_ID, update.received!!.accountId!!) } returns true

        shouldThrowExactly<InvalidCategoryException> {
            transferModificationImpl.modify(USER_ID, update)
        }

        verify { eventPublisher wasNot Called }
    }

    @Test
    fun `modify throws exception when received account is provided and absent in DB`() {
        every {
            transferStorage.findByIdAndUserIdOrThrow(
                DEFAULT_TRANSFER.id,
                USER_ID
            )
        } returns DEFAULT_TRANSFER

        val update = transferUpdate(DEFAULT_TRANSFER.id) {
            received = AccountMoney(
                money = PositiveMoney(Currency.EUR, 10, 1),
                accountId = "test".asId()
            )
        }

        every { transferCategoryValidator.validate(USER_ID, DEFAULT_TRANSFER.categoryId) } returns true
        every { transferAccountValidator.validate(USER_ID, update.received!!.accountId!!) } returns false

        shouldThrowExactly<InvalidAccountException> {
            transferModificationImpl.modify(USER_ID, update)
        }
        verify { eventPublisher wasNot Called }
    }

    @Test
    fun `modify throws exception when send account is provided and absent in DB`() {
        every {
            transferStorage.findByIdAndUserIdOrThrow(
                DEFAULT_TRANSFER.id,
                USER_ID
            )
        } returns DEFAULT_TRANSFER

        val update = transferUpdate(DEFAULT_TRANSFER.id) {
            send = AccountMoney(
                money = PositiveMoney(Currency.EUR, 10, 1),
                accountId = "test".asId()
            )
        }

        every { transferCategoryValidator.validate(USER_ID, DEFAULT_TRANSFER.categoryId) } returns true
        every { transferAccountValidator.validate(USER_ID, update.send!!.accountId!!) } returns false

        shouldThrowExactly<InvalidAccountException> {
            transferModificationImpl.modify(USER_ID, update)
        }
        verify { eventPublisher wasNot Called }
    }

    @Test
    fun `modify modifies transfer when all necessary data is provided`() {
        every {
            transferStorage.findByIdAndUserIdOrThrow(
                DEFAULT_TRANSFER.id,
                USER_ID
            )
        } returns DEFAULT_TRANSFER

        val update = transferUpdate(DEFAULT_TRANSFER.id) {
            received = AccountMoney(
                money = PositiveMoney(Currency.EUR, 10, 1),
                accountId = "test".asId()
            )
            description = "tttt"
        }

        every { transferStorage.update(DEFAULT_TRANSFER.updateTransfer(update)) } returns DEFAULT_TRANSFER.updateTransfer(
            update
        )

        every { transferCategoryValidator.validate(USER_ID, DEFAULT_TRANSFER.categoryId) } returns true
        every { transferAccountValidator.validate(USER_ID, update.received!!.accountId!!) } returns true

        val result = transferModificationImpl.modify(USER_ID, update)

        result.transfer.description shouldBe update.description
        verify(exactly = 1) { eventPublisher.publish(DEFAULT_TRANSFER.toRollbackMoneyIsTransferredEvent()) }
        verify(exactly = 1) {
            eventPublisher.publish(
                DEFAULT_TRANSFER.updateTransfer(update).toMoneyIsTransferredEvent()
            )
        }
    }

    private companion object {
        val USER_ID = 543.asId<User>()
        val DEFAULT_TRANSFER = transfer(654.asId(), USER_ID) {
            description = "test description"
        }
    }
}