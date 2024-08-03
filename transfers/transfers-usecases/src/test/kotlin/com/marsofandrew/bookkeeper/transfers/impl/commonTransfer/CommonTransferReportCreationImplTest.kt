package com.marsofandrew.bookkeeper.transfers.impl.commonTransfer

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.exception.InvalidDateIntervalException
import com.marsofandrew.bookkeeper.transfers.fixtures.commonTransfer
import com.marsofandrew.bookkeeper.transfers.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CommonTransferReportCreationImplTest {

    private val transferStorage = mockk<TransferStorage>()

    private lateinit var creatingTransfersReportImpl: CommonTransfersReportCreationImpl

    @BeforeEach
    fun setup() {
        creatingTransfersReportImpl = CommonTransfersReportCreationImpl(transferStorage)
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
            commonTransfer(1.asId(), userId) {
                date = startDate
                received = AccountMoney.create(PositiveMoney(Currency.EUR, BigDecimal(5)))
            },
            commonTransfer(2.asId(), userId) {
                date = endDate
                received = AccountMoney.create(PositiveMoney(Currency.EUR, 42, 1))
            },
            commonTransfer(3.asId(), userId) {
                date = startDate
                send = AccountMoney.create(PositiveMoney(Currency.EUR, 31, 1))
                received = AccountMoney.create(PositiveMoney(Currency.USD, BigDecimal(4)))
            },
            commonTransfer(4.asId(), userId) {
                date = endDate
                send = AccountMoney.create(PositiveMoney(Currency.USD, BigDecimal(2)))
                received = AccountMoney.create(PositiveMoney(Currency.EUR, 25, 1))
            },
            commonTransfer(5.asId(), userId) {
                date = endDate
                send = AccountMoney.create(PositiveMoney(Currency.USD, BigDecimal(3)))
                received = AccountMoney.create(PositiveMoney(Currency.RUB, 3005, 1))
            }
        )

        val report = creatingTransfersReportImpl.createReport(userId, startDate, endDate)

        report.total shouldContainExactlyInAnyOrder listOf(
            Money(Currency.RUB, 30050, 2),
            Money(Currency.USD, -1, 0),
            Money(Currency.EUR, 86, 1)
        )
    }

    @Test
    fun `createReport throws exception when invalid date interval is provided`() {
        val now = LocalDate.now()
        shouldThrowExactly<InvalidDateIntervalException> {
            creatingTransfersReportImpl.createReport(
                5.asId(),
                now,
                now.minusDays(1)
            )
        }
    }
}