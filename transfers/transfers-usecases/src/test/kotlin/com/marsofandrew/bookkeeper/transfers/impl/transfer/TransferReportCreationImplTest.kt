package com.marsofandrew.bookkeeper.transfers.impl.transfer

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.fixtures.transfer
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

internal class TransferReportCreationImplTest {

    private val transferStorage = mockk<TransferStorage>()

    private lateinit var transferReportCreationImpl: TransferReportCreationImpl

    @BeforeEach
    fun setup() {
        transferReportCreationImpl = TransferReportCreationImpl(transferStorage)
    }

    @Test
    fun `createReport when transfers are absent returns empty report`() {
        val userId = 5.asId<User>()
        val now = LocalDate.now()
        val startDate = now.minusDays(1)
        val endDate = now

        every { transferStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns emptyList()

        val report = transferReportCreationImpl.createReport(userId, startDate, endDate)

        report.total shouldBe emptyList()
    }

    @Test
    fun `createReport when transfers exists returns report about that transfers`() {
        val userId = 5.asId<User>()
        val now = LocalDate.now()
        val startDate = now.minusDays(1)
        val endDate = now

        every { transferStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns listOf(
            transfer(3.asId(), userId) {
                date = startDate
                send = AccountMoney.create(PositiveMoney(Currency.EUR, 31, 1))
                received = AccountMoney.create(PositiveMoney(Currency.USD, BigDecimal(4)))
            },
            transfer(4.asId(), userId) {
                date = endDate
                send = AccountMoney.create(PositiveMoney(Currency.USD, BigDecimal(2)))
                received = AccountMoney.create(PositiveMoney(Currency.EUR, 25, 1))
            },
            transfer(5.asId(), userId) {
                date = endDate
                send = AccountMoney.create(PositiveMoney(Currency.USD, BigDecimal(3)))
                received = AccountMoney.create(PositiveMoney(Currency.RUB, 3005, 1))
            }
        )

        val report = transferReportCreationImpl.createReport(userId, startDate, endDate)

        report.total shouldContainExactlyInAnyOrder listOf(
            Money(Currency.RUB, 30050, 2),
            Money(Currency.USD, -1, 0),
            Money(Currency.EUR, -6, 1)
        )
    }

    @Test
    fun `createReport throws exception when invalid date interval is provided`() {
        val now = LocalDate.now()
        shouldThrowExactly<IllegalArgumentException> {
            transferReportCreationImpl.createReport(
                5.asId(),
                now,
                now.minusDays(1)
            )
        }
    }
}