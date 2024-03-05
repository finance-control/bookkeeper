package com.marsofandrew.bookkeeper.transfer.access

import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.fixtures.transfer
import com.marsofandrew.bookkeeper.transfer.user.User
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferStorageImplTest {
    private val transfersByUserId: MutableMap<NumericId<User>, MutableSet<Transfer>> = mutableMapOf()
    private val transferById: MutableMap<StringId<Transfer>, Transfer> = mutableMapOf()

    private lateinit var transferstorageImpl: TransferStorageImpl

    @BeforeEach
    fun setup() {
        transfersByUserId.clear()
        transferById.clear()
        transferstorageImpl = TransferStorageImpl(transfersByUserId, transferById)
    }

    @Test
    fun `create when correct values are provided then creates transfer with unique id`() {
        val transfer = transfer(StringId.unidentified(), userId)

        val savedtransfer = transferstorageImpl.create(transfer)

        savedtransfer.id.initialized shouldBe true

        transfersByUserId.entries shouldHaveSize 1
        transferById.entries shouldHaveSize 1

        savedtransfer.userId shouldBe userId
        savedtransfer.received shouldBe transfer.received
        savedtransfer.send shouldBe transfer.send
    }

    @Test
    fun `delete does nothing when ids are not exists`() {
        transferstorageImpl.delete(setOf(StringId("test")))

        transfersByUserId.isEmpty() shouldBe true
        transferById.isEmpty() shouldBe true
    }

    @Test
    fun `delete deletes transfers by ids`() {
        val transfer = transfer(StringId.unidentified(), userId)
        val savedtransfer = transferstorageImpl.create(transfer)

        transferstorageImpl.delete(setOf(savedtransfer.id))

        (transfersByUserId[userId] == null || transfersByUserId[userId]!!.isEmpty()) shouldBe true
        transferById.isEmpty() shouldBe true
    }

    @Test
    fun `findAllByUserId when transfers are absent returns empty list`() {
        val result = transferstorageImpl.findAllByUserId(userId)

        result shouldHaveSize 0
    }

    @Test
    fun `findAllByUserId when transfers for user exist returns those transfers`() {
        val transfers = listOf(
            transfer(StringId.unidentified(), userId),
            transfer(StringId.unidentified(), userId) { received = PositiveMoney(Currency.EUR, 100) },
        ).map(transferstorageImpl::create)

        val result = transferstorageImpl.findAllByUserId(userId)

        result shouldContainExactlyInAnyOrder transfers
    }

    @Test
    fun `findAllByUserIdBetween when transfers are absent returns empty list`() {
        val now = LocalDate.now()

        val result = transferstorageImpl.findAllByUserIdBetween(userId, now.minusDays(1), now)

        result shouldHaveSize 0
    }

    @Test
    fun `findAllByUserIdBetween when transfers within interval are absent returns empty list`() {
        val now = LocalDate.now()
        transferstorageImpl.create(transfer(StringId.unidentified(), userId) { date = now.plusDays(1) })


        val result = transferstorageImpl.findAllByUserIdBetween(userId, now.minusDays(1), now)

        result shouldHaveSize 0
    }

    @Test
    fun `findAllByUserIdBetween when transfers for user within interval exist returns those transfers`() {
        val now = LocalDate.now()
        val startDate = now.minusDays(2)

        val transfers = listOf(
            transfer(StringId.unidentified(), userId) { date = now },
            transfer(StringId.unidentified(), userId) { date = startDate },
            transfer(StringId.unidentified(), userId) { date = startDate.minusDays(1) },
            transfer(StringId.unidentified(), userId) { date = now.plusDays(1) },
            transfer(StringId.unidentified(), userId) { date = now },
        ).map(transferstorageImpl::create)

        val result = transferstorageImpl.findAllByUserIdBetween(userId, startDate, now)

        result shouldContainExactlyInAnyOrder transfers
            .filter { transfer -> transfer.date in startDate..now }
    }

    private companion object {
        val userId = 5.asId<User>()
    }
}