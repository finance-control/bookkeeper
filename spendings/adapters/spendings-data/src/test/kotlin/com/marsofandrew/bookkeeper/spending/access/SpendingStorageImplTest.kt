package com.marsofandrew.bookkeeper.spending.access

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.fixtures.spending
import com.marsofandrew.bookkeeper.spending.user.User
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingStorageImplTest {

    private val spendingsByUserId: MutableMap<NumericId<User>, MutableSet<Spending>> = mutableMapOf()
    private val spendingById: MutableMap<StringId<Spending>, Spending> = mutableMapOf()

    private lateinit var spendingStorageImpl: SpendingStorageImpl

    @BeforeEach
    fun setup() {
        spendingsByUserId.clear()
        spendingById.clear()
        spendingStorageImpl = SpendingStorageImpl(spendingsByUserId, spendingById)
    }

    @Test
    fun `create when correct values are provided then creates spending with unique id`() {
        val spending = spending(StringId.unidentified(), userId)

        val savedSpending = spendingStorageImpl.create(spending)

        savedSpending.id.initialized shouldBe true

        spendingsByUserId.entries shouldHaveSize 1
        spendingById.entries shouldHaveSize 1

        savedSpending.userId shouldBe userId
        savedSpending.money shouldBe spending.money
    }

    @Test
    fun `delete does nothing when ids are not exists`() {
        spendingStorageImpl.delete(setOf(StringId("test")))

        spendingsByUserId.isEmpty() shouldBe true
        spendingById.isEmpty() shouldBe true
    }

    @Test
    fun `delete deletes spendings by ids`() {
        val spending = spending(StringId.unidentified(), userId)
        val savedSpending = spendingStorageImpl.create(spending)

        spendingStorageImpl.delete(setOf(savedSpending.id))

        (spendingsByUserId[userId] == null || spendingsByUserId[userId]!!.isEmpty()) shouldBe true
        spendingById.isEmpty() shouldBe true
    }

    @Test
    fun `findAllByUserId when spendings are absent returns empty list`() {
        val result = spendingStorageImpl.findAllByUserId(userId)

        result shouldHaveSize 0
    }

    @Test
    fun `findAllByUserId when spendings for user exist returns those spendings`() {
        val spendings = listOf(
            spending(StringId.unidentified(), userId),
            spending(StringId.unidentified(), userId) { money = PositiveMoney(Currency.EUR, 100) },
        ).map(spendingStorageImpl::create)

        val result = spendingStorageImpl.findAllByUserId(userId)

        result shouldContainExactlyInAnyOrder spendings
    }

    @Test
    fun `findAllByUserIdBetween when spendings are absent returns empty list`() {
        val now = LocalDate.now()

        val result = spendingStorageImpl.findAllByUserIdBetween(userId, now.minusDays(1), now)

        result shouldHaveSize 0
    }

    @Test
    fun `findAllByUserIdBetween when spendings within interval are absent returns empty list`() {
        val now = LocalDate.now()
        spendingStorageImpl.create(spending(StringId.unidentified(), userId) { date = now.plusDays(1) })


        val result = spendingStorageImpl.findAllByUserIdBetween(userId, now.minusDays(1), now)

        result shouldHaveSize 0
    }

    @Test
    fun `findAllByUserIdBetween when spendings for user within interval exist returns those spendings`() {
        val now = LocalDate.now()
        val startDate = now.minusDays(2)

        val spendings = listOf(
            spending(StringId.unidentified(), userId) { date = now },
            spending(StringId.unidentified(), userId) { date = startDate },
            spending(StringId.unidentified(), userId) { date = startDate.minusDays(1) },
            spending(StringId.unidentified(), userId) { date = now.plusDays(1) },
            spending(StringId.unidentified(), userId) { date = now },
        ).map(spendingStorageImpl::create)

        val result = spendingStorageImpl.findAllByUserIdBetween(userId, startDate, now)

        result shouldContainExactlyInAnyOrder spendings
            .filter { spending -> spending.date in startDate..now }
    }

    private companion object {
        val userId = 5.asId<User>()
    }
}