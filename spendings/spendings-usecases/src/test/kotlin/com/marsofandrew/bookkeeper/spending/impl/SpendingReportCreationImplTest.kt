package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.category.Category
import com.marsofandrew.bookkeeper.spending.exception.InvalidDateIntervalException
import com.marsofandrew.bookkeeper.spending.fixture.spending
import com.marsofandrew.bookkeeper.spending.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingReportCreationImplTest {

    private val spendingStorage = mockk<SpendingStorage>()
    private lateinit var creatingSpendingReportImpl: SpendingReportCreationImpl;

    @BeforeEach
    fun setup() {
        creatingSpendingReportImpl = SpendingReportCreationImpl(spendingStorage)
    }

    @Test
    fun `createReport when no data is available returns empty report`() {
        val userId = 5.asId<User>()
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(2)

        every { spendingStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns emptyList()

        val report = creatingSpendingReportImpl.createReport(userId, startDate, endDate)

        report.total shouldHaveSize 0
        report.spendingByCategory shouldBe emptyMap()
    }

    @Test
    fun `createReport when data is available returns report`() {
        val userId = 5.asId<User>()
        val category1 = NumericId<Category>(3)
        val category2 = NumericId<Category>(1)
        val category3 = NumericId<Category>(2)
        val date1 = LocalDate.now()
        val date2 = date1.minusDays(2)

        val spendings = listOf(
            spending(1.asId(), userId) {
                categoryId = category1
                date = date1
                money = PositiveMoney(Currency.USD, BigDecimal(15))
            },
            spending(2.asId(), userId) {
                categoryId = category1
                date = date2
                money = PositiveMoney(Currency.EUR, BigDecimal(15))
            },
            spending(3.asId(), userId) {
                categoryId = category2
                date = date1
                money = PositiveMoney(Currency.EUR, BigDecimal.valueOf(50.5))
            },
            spending(4.asId(), userId) {
                categoryId = category2
                date = date2
                money = PositiveMoney(Currency.EUR, BigDecimal.valueOf(42.25))
            },
            spending(5.asId(), userId) {
                categoryId = category3
                date = date1
                money = PositiveMoney(Currency.USD, BigDecimal(30.5))
            },
        )

        every { spendingStorage.findAllByUserIdBetween(userId, date2, date1) } returns spendings

        val report = creatingSpendingReportImpl.createReport(userId, date2, date1)

        report.total shouldContainExactlyInAnyOrder listOf(
            PositiveMoney(Currency.USD, BigDecimal(45.5)),
            PositiveMoney(Currency.EUR, BigDecimal(107.75))
        )
        report.spendingByCategory.keys shouldContainExactlyInAnyOrder listOf(category1, category2, category3)
        report.spendingByCategory[category1] shouldContainExactlyInAnyOrder listOf(
            PositiveMoney(Currency.USD, BigDecimal(15)),
            PositiveMoney(Currency.EUR, BigDecimal(15))
        )
        report.spendingByCategory[category2] shouldContainExactlyInAnyOrder listOf(
            PositiveMoney(
                Currency.EUR,
                BigDecimal(92.75)
            )
        )
        report.spendingByCategory[category3] shouldContainExactlyInAnyOrder listOf(
            PositiveMoney(Currency.USD, BigDecimal(30.5))
        )
    }

    @Test
    fun `createReport when data is available and categories are provided returns report only with provided categories`() {
        val userId = 5.asId<User>()
        val category1 = NumericId<Category>(1)
        val category2 = NumericId<Category>(2)
        val category3 = NumericId<Category>(3)
        val now = LocalDate.now()
        val date1 = now.minusDays(1)
        val date2 = now.minusDays(2)

        val spendings = listOf(
            spending(1.asId(), userId) {
                categoryId = category1
                date = date1
                money = PositiveMoney(Currency.USD, BigDecimal(15))
            },
            spending(2.asId(), userId) {
                categoryId = category1
                date = date2
                money = PositiveMoney(Currency.EUR, BigDecimal(15))
            },
            spending(3.asId(), userId) {
                categoryId = category2
                date = date1
                money = PositiveMoney(Currency.EUR, BigDecimal.valueOf(50.5))
            },
            spending(4.asId(), userId) {
                categoryId = category2
                date = date2
                money = PositiveMoney(Currency.EUR, BigDecimal.valueOf(42.25))
            },
            spending(5.asId(), userId) {
                categoryId = category3
                date = date1
                money = PositiveMoney(Currency.USD, BigDecimal(30.5))
            },
        )

        every { spendingStorage.findAllByUserIdBetween(userId, date2, date1) } returns spendings

        val report =
            creatingSpendingReportImpl.createReport(userId, date2, date1, categories = setOf(category1, category2))

        report.total shouldContainExactlyInAnyOrder listOf(
            PositiveMoney(Currency.USD, BigDecimal(15)),
            PositiveMoney(Currency.EUR, BigDecimal(107.75))
        )
        report.spendingByCategory.keys shouldContainExactlyInAnyOrder listOf(category1, category2)
        report.spendingByCategory[category1] shouldContainExactlyInAnyOrder listOf(
            PositiveMoney(Currency.USD, BigDecimal(15)),
            PositiveMoney(Currency.EUR, BigDecimal(15))
        )
        report.spendingByCategory[category2] shouldContainExactlyInAnyOrder listOf(
            PositiveMoney(
                Currency.EUR,
                BigDecimal(92.75)
            )
        )
    }

    @Test
    fun `createReport throws exception when invalid date interval is provided`() {
        val now = LocalDate.now()
        shouldThrowExactly<InvalidDateIntervalException> {
            creatingSpendingReportImpl.createReport(
                5.asId(),
                now,
                now.minusDays(1)
            )
        }
    }
}
