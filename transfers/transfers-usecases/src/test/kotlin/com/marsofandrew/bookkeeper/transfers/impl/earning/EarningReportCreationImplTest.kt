package com.marsofandrew.bookkeeper.transfers.impl.earning

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.fixtures.earning
import com.marsofandrew.bookkeeper.transfers.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

internal class EarningReportCreationImplTest {

    private val transferStorage = mockk<TransferStorage>()

    private lateinit var earningReportCreation: EarningReportCreationImpl

    @BeforeEach
    fun setup() {
        earningReportCreation = EarningReportCreationImpl(transferStorage)
    }

    @Test
    fun `createReport when transfers are absent returns empty report`() {
        val userId = 5.asId<User>()
        val now = LocalDate.now()
        val startDate = now.minusDays(1)
        val endDate = now

        every { transferStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns emptyList()

        val report = earningReportCreation.createReport(userId, startDate, endDate)

        report.total shouldBe emptyList()
    }

    @Test
    fun `createReport when earnings exists returns report about that earnings`() {
        val userId = 5.asId<User>()
        val now = LocalDate.now()
        val startDate = now.minusDays(1)
        val endDate = now

        every { transferStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns listOf(
            earning(1.asId(), userId) {
                date = startDate
                received = AccountMoney.create(PositiveMoney(Currency.EUR, BigDecimal(5)))
            },
            earning(2.asId(), userId) {
                date = endDate
                received = AccountMoney.create(PositiveMoney(Currency.EUR, 42, 1))
            },
            earning(5.asId(), userId) {
                date = endDate
                received = AccountMoney.create(PositiveMoney(Currency.RUB, 3005, 1))
            }
        )

        val report = earningReportCreation.createReport(userId, startDate, endDate)

        report.total shouldContainExactlyInAnyOrder listOf(
            Money(Currency.RUB, 30050, 2),
            Money(Currency.EUR, 92, 1)
        )
    }

    @Test
    fun `createReport throws exception when invalid date interval is provided`() {
        val now = LocalDate.now()
        shouldThrowExactly<IllegalArgumentException> {
            earningReportCreation.createReport(
                5.asId(),
                now,
                now.minusDays(1)
            )
        }
    }
}