package com.marsofandrew.bookkeeper.spending.access

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.fixture.spending
import com.marsofandrew.bookkeeper.spending.user.User
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingStorageImplTest {

    private lateinit var spendingStorageImpl: SpendingStorageImpl

    @BeforeEach
    fun setup() {
        //spendingStorageImpl = SpendingStorageImpl(spendingsByUserId, spendingById)
    }

    @Test
    fun `create when correct values are provided then creates spending with unique id`() {
        val spending = spending(NumericId.unidentified(), userId)
    }

    @Test
    fun `delete does nothing when ids are not exists`() {

    }

    @Test
    fun `delete deletes spendings by ids`() {

    }

    @Test
    fun `findAllByUserId when spendings are absent returns empty list`() {

    }

    @Test
    fun `findAllByUserId when spendings for user exist returns those spendings`() {
        val spendings = listOf(
            spending(NumericId.unidentified(), userId),
            spending(NumericId.unidentified(), userId) { money = PositiveMoney(Currency.EUR, 100) },
        )
    }

    @Test
    fun `findAllByUserIdBetween when spendings are absent returns empty list`() {

    }

    @Test
    fun `findAllByUserIdBetween when spendings within interval are absent returns empty list`() {

    }

    @Test
    fun `findAllByUserIdBetween when spendings for user within interval exist returns those spendings`() {
    }

    private companion object {
        val userId = 5.asId<User>()
    }
}