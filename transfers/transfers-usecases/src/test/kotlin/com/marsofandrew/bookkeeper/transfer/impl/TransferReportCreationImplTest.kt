package com.marsofandrew.bookkeeper.transfer.impl

import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.fixtures.transfer
import com.marsofandrew.bookkeeper.transfer.user.User
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransferReportCreationImplTest {

    private val transferStorage = mockk<TransferStorage>()

    private lateinit var creatingTransfersReportImpl: TransfersReportCreationImpl

    @BeforeEach
    fun setup() {
        creatingTransfersReportImpl = TransfersReportCreationImpl(transferStorage)
    }

    @Test
    fun `createReport when transfers are absent returns empty report`() {
        val userId = 5.asId<User>()
        val now = LocalDate.now()
        val startDate = now.minusDays(1)
        val endDate = now

        every { transferStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns emptyList()

        val report = creatingTransfersReportImpl.createReport(userId, startDate, endDate)

        report.total shouldBe emptyList()
    }

    @Test
    fun `createReport when transfers exists returns report about that transfers`() {
        val userId = 5.asId<User>()
        val now = LocalDate.now()
        val startDate = now.minusDays(1)
        val endDate = now

        every { transferStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns listOf(
            transfer("1".asId(), userId) {
                date = startDate
                received = PositiveMoney(Currency.EUR, BigDecimal(5))
            },
            transfer("2".asId(), userId) {
                date = endDate
                received = PositiveMoney(Currency.EUR, 42, 1)
            },
            transfer("3".asId(), userId) {
                date = startDate
                send = PositiveMoney(Currency.EUR, 31, 1)
                received = PositiveMoney(Currency.USD, BigDecimal(4))
            },
            transfer("4".asId(), userId) {
                date = endDate
                send = PositiveMoney(Currency.USD, BigDecimal(2))
                received = PositiveMoney(Currency.EUR, 25, 1)
            },
            transfer("5".asId(), userId) {
                date = endDate
                send = PositiveMoney(Currency.USD, BigDecimal(3))
                received = PositiveMoney(Currency.RUB, 3005, 1)
            }
        )

        val report = creatingTransfersReportImpl.createReport(userId, startDate, endDate)

        report.total shouldContainExactlyInAnyOrder listOf(
            Money(Currency.RUB, BigDecimal(30025).movePointLeft(2)),
            Money(Currency.USD, BigDecimal(-1)),
            Money(Currency.EUR, 86, 1)
        )
    }

    @Test
    fun `createReport throws exception when invalid date interval is provided`() {
        val now = LocalDate.now()
        shouldThrowExactly<IllegalArgumentException> {
            creatingTransfersReportImpl.createReport(
                5.asId(),
                now,
                now.minusDays(1)
            )
        }
    }
}