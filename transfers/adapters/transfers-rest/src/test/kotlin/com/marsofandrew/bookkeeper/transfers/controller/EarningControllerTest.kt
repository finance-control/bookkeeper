package com.marsofandrew.bookkeeper.transfers.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.TransferReport
import com.marsofandrew.bookkeeper.transfers.controller.dto.*
import com.marsofandrew.bookkeeper.transfers.controller.dto.AccountMoneyDto
import com.marsofandrew.bookkeeper.transfers.controller.dto.CreateTransferDto
import com.marsofandrew.bookkeeper.transfers.controller.dto.PositiveMoneyDto
import com.marsofandrew.bookkeeper.transfers.controller.dto.toAccountMoney
import com.marsofandrew.bookkeeper.transfers.earning.EarningAdding
import com.marsofandrew.bookkeeper.transfers.earning.EarningModification
import com.marsofandrew.bookkeeper.transfers.earning.EarningReportCreation
import com.marsofandrew.bookkeeper.transfers.earning.EarningSelection
import com.marsofandrew.bookkeeper.transfers.fixtures.earning
import com.marsofandrew.bookkeeper.transfers.fixtures.earningUpdate
import com.marsofandrew.bookkeeper.userContext.AuthArgumentContextConfiguration
import com.marsofandrew.bookkeeper.userContext.UserIdToken
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

@WebMvcTest
@ContextConfiguration(
    classes = [
        EarningControllerTest.TestContextConfiguration::class,
        AuthArgumentContextConfiguration::class
    ]
)
internal class EarningControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        clearAllMocks()
        SecurityContextHolder.getContext().authentication = UserIdToken(userId)
    }

    @Test
    fun `add when correct values are set when creates the earning and returns id`() {
        val now = LocalDate.now()
        val createTransferDto = CreateTransferDto(
            date = now,
            send = AccountMoneyDto(PositiveMoneyDto(5, 0, "USD")),
            received = AccountMoneyDto(PositiveMoneyDto(4, 0, "EUR")),
            description = "",
            categoryId = 1
        )
        val earning = Earning(
            NumericId.unidentified(),
            userId.asId(),
            createTransferDto.date,
            createTransferDto.received.toAccountMoney(),
            createTransferDto.description,
            createTransferDto.categoryId.asId(),
            LocalDate.ofInstant(clock.instant(), ZoneId.of("Z")),
            Version(0)
        )
        val identifiedTransfer = earning.copy(id = 100.asId())

        every { earningAdding.add(earning) } returns identifiedTransfer

        mockMvc.post("/api/v1/earnings") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createTransferDto)
        }.andExpect {
            status { isOk() }
            jsonPath("id") { value(identifiedTransfer.id.value) }
            jsonPath("userId") { value(userId) }
            jsonPath("received.money.amount") {
                value(4)
            }
            jsonPath("received.money.digits") { value(0) }
            jsonPath("received.money.currencyCode") { value("EUR") }
            jsonPath("date") { value(now.toString()) }
        }

        verify(exactly = 1) { earningAdding.add(earning) }
    }

    @Test
    fun `get returns earnings`() {
        val now = LocalDate.now()
        val earnings = listOf(
            earning(1.asId(), userId.asId()),
            earning(2.asId(), userId.asId()),
            earning(3.asId(), userId.asId())
        )

        every { earningSelection.select(userId.asId(), null, now) } returns earnings

        mockMvc.get("/api/v1/earnings?end_date=${now}")
            .andExpect {
                status { isOk() }
                jsonPath("$") {
                    isArray()
                }
            }

        verify(exactly = 1) { earningSelection.select(userId.asId(), null, now) }
    }

    @Test
    fun `report returns report`() {
        val now = LocalDate.now()
        val startDate = now.minusDays(1)
        val endDate = now.plusDays(1)
        val transferReport = TransferReport(
            total = listOf(Money(Currency.EUR, 5, 0), Money(Currency.USD, -5, 0))
        )

        every { earningReportCreation.createReport(userId.asId(), startDate, endDate) } returns transferReport

        mockMvc.get("/api/v1/earnings/report?end_date=${endDate}&start_date=${startDate}")
            .andExpect {
                status { isOk() }
                jsonPath("total[0].currencyCode") { value(Currency.EUR.name) }
                jsonPath("total[1].currencyCode") { value(Currency.USD.name) }
                jsonPath("total[1].amount") { value(-5) }
            }

        verify(exactly = 1) { earningReportCreation.createReport(userId.asId(), startDate, endDate) }
    }

    @Test
    fun `patch modifies earning`() {
        val update = earningUpdate(1.asId()) {
            description = "description"
        }

        val updateEarningDto = UpdateEarningDto(
            received = null,
            date = null,
            description = update.description,
            categoryId = null,
            version = 0
        )

        val identifiedEarning = earning(update.id, userId.asId())

        every { earningModification.modify(userId.asId(), update) } returns identifiedEarning

        mockMvc.patch("/api/v1/earnings/${identifiedEarning.id.value}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updateEarningDto)
        }.andExpect {
            status { isOk() }
            jsonPath("id") { value(identifiedEarning.id.value) }
            jsonPath("userId") { value(userId) }
        }

        verify(exactly = 1) { earningModification.modify(userId.asId(), update) }
    }

    @ContextConfiguration
    class TestContextConfiguration {

        @Primary
        @Bean
        fun earningController() = EarningController(
            earningAdding,
            earningSelection,
            earningReportCreation,
            earningModification,
            clock
        )
    }

    private companion object {
        const val userId = 5L

        val earningAdding = mockk<EarningAdding>()
        val earningSelection = mockk<EarningSelection>()
        val earningReportCreation = mockk<EarningReportCreation>()
        val earningModification = mockk<EarningModification>()
        val clock = Clock.fixed(Instant.now(), ZoneId.of("Z"))
    }
}