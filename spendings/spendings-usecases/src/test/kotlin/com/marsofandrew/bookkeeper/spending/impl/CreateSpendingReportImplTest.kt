package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.category.SpendingCategory
import com.marsofandrew.bookkeeper.spending.fixtures.spending
import com.marsofandrew.bookkeeper.spending.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CreateSpendingReportImplTest {

    private val spendingStorage = mockk<SpendingStorage>()
    private lateinit var creatingSpendingReportImpl: SpendingReportCreationImpl;

    @BeforeEach
    fun setup() {
        creatingSpendingReportImpl = SpendingReportCreationImpl(spendingStorage)
    }

    @Test
    fun `createReport when no data is available returns empty report`() {
        val userId = 5.asId<User>()
        val endDate = LocalDate.from(Instant.now())
        val startDate = LocalDate.from(Instant.now().minus(Duration.ofDays(1)))

        every { spendingStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns emptyList()

        val report = creatingSpendingReportImpl.createReport(userId, startDate, endDate)

        report.total shouldHaveSize 0
        report.spendingByCategory shouldBe emptyMap()
    }

    @Test
    fun `createReport when data is available returns report`() {
        val userId = 5.asId<User>()
        val category1 = NumericId<SpendingCategory>(0)
        val category2 = NumericId<SpendingCategory>(1)
        val category3 = NumericId<SpendingCategory>(2)
        val now = Instant.now()
        val date1 = LocalDate.from(now.minusSeconds(1000))
        val date2 = LocalDate.from(now.minus(Duration.ofDays(1)))

        val spendings = listOf(
            spending(1.asId(), userId) {
                spendingCategoryId = category1
                date = date1
                money = PositiveMoney(Currency.USD, BigDecimal(15))
            },
            spending(2.asId(), userId) {
                spendingCategoryId = category1
                date = date2
                money = PositiveMoney(Currency.EUR, BigDecimal(15))
            },
            spending(3.asId(), userId) {
                spendingCategoryId = category2
                date = date1
                money = PositiveMoney(Currency.EUR, BigDecimal.valueOf(50.5))
            },
            spending(4.asId(), userId) {
                spendingCategoryId = category2
                date = date2
                money = PositiveMoney(Currency.EUR, BigDecimal.valueOf(42.25))
            },
            spending(5.asId(), userId) {
                spendingCategoryId = category3
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
        val category1 = NumericId<SpendingCategory>(1)
        val category2 = NumericId<SpendingCategory>(2)
        val category3 = NumericId<SpendingCategory>(3)
        val now = Instant.now()
        val date1 = LocalDate.from(now.minusSeconds(1000))
        val date2 = LocalDate.from(now.minus(Duration.ofDays(1)))

        val spendings = listOf(
            spending(1.asId(), userId) {
                spendingCategoryId = category1
                date = date1
                money = PositiveMoney(Currency.USD, BigDecimal(15))
            },
            spending(2.asId(), userId) {
                spendingCategoryId = category1
                date = date2
                money = PositiveMoney(Currency.EUR, BigDecimal(15))
            },
            spending(3.asId(), userId) {
                spendingCategoryId = category2
                date = date1
                money = PositiveMoney(Currency.EUR, BigDecimal.valueOf(50.5))
            },
            spending(4.asId(), userId) {
                spendingCategoryId = category2
                date = date2
                money = PositiveMoney(Currency.EUR, BigDecimal.valueOf(42.25))
            },
            spending(5.asId(), userId) {
                spendingCategoryId = category3
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
        val now = Instant.now()
        shouldThrowExactly<IllegalArgumentException> {
            creatingSpendingReportImpl.createReport(
                5.asId(),
                LocalDate.from(now),
                LocalDate.from(now.minus(Duration.ofDays(1)))
            )
        }
    }

}