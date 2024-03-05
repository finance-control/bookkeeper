package com.marsofandrew.bookkeeper.transfer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.TransferAdding
import com.marsofandrew.bookkeeper.transfer.TransferDeletion
import com.marsofandrew.bookkeeper.transfer.TransferReport
import com.marsofandrew.bookkeeper.transfer.TransferReportCreation
import com.marsofandrew.bookkeeper.transfer.TransferSelection
import com.marsofandrew.bookkeeper.transfer.controller.dto.AccountMoneyDto
import com.marsofandrew.bookkeeper.transfer.controller.dto.CreateTransferDto
import com.marsofandrew.bookkeeper.transfer.controller.dto.PositiveMoneyDto
import com.marsofandrew.bookkeeper.transfer.controller.dto.toAccountMoney
import com.marsofandrew.bookkeeper.transfer.fixtures.transfer
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
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest
@ContextConfiguration(
    classes = [
        TransferControllerTest.TestContextConfiguration::class,
        AuthArgumentContextConfiguration::class]
)
internal class TransferControllerTest {

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
    fun `add when correct values are set when creates the transfer and returns id`() {
        val now = LocalDate.now()
        val createTransferDto = CreateTransferDto(
            date = now,
            send = AccountMoneyDto(PositiveMoneyDto(5, 0, "USD")),
            received = AccountMoneyDto(PositiveMoneyDto(4, 0, "EUR")),
            description = "",
            transferCategoryId = 1
        )
        val transfer = Transfer(
            NumericId.unidentified(),
            userId.asId(),
            createTransferDto.date,
            requireNotNull(createTransferDto.send).toAccountMoney(),
            createTransferDto.received.toAccountMoney(),
            createTransferDto.description,
            createTransferDto.transferCategoryId.asId(),
            LocalDate.ofInstant(clock.instant(), ZoneId.of("Z"))
        )
        val identifiedTransfer = transfer.copy(id = 100.asId())

        every { transferAdding.add(transfer) } returns identifiedTransfer

        mockMvc.post("/api/v1/transfers") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createTransferDto)
        }.andExpect {
            status { isOk() }
            jsonPath("id") { value(identifiedTransfer.id.value) }
            jsonPath("userId") { value(userId) }
            jsonPath("send.money.amount") {
                value(5)
            }
            jsonPath("send.money.digits") { value(0) }
            jsonPath("send.money.currencyCode") { value("USD") }
            jsonPath("received.money.amount") {
                value(4)
            }
            jsonPath("received.money.digits") { value(0) }
            jsonPath("received.money.currencyCode") { value("EUR") }
            jsonPath("date") { value(now.toString()) }
        }

        verify(exactly = 1) { transferAdding.add(transfer) }
    }

    @Test
    fun `deletes deletes when provided id exists`() {
        val id = 55
        every { transferDeletion.delete(userId.asId(), setOf(id.asId())) } returns Unit

        mockMvc.delete("/api/v1/transfers/{id}", id).andExpect {
            status { isNoContent() }
        }

        verify(exactly = 1) { transferDeletion.delete(userId.asId(), setOf(id.asId())) }
    }

    @Test
    fun `get returns transfers`() {
        val now = LocalDate.now()
        val transfers = listOf(
            transfer(1.asId(), userId.asId()),
            transfer(2.asId(), userId.asId()),
            transfer(3.asId(), userId.asId())
        )

        every { transferSelection.select(userId.asId(), null, now) } returns transfers

        mockMvc.get("/api/v1/transfers?end_date=${now}")
            .andExpect {
                status { isOk() }
                jsonPath("$") {
                    isArray()
                }
            }

        verify(exactly = 1) { transferSelection.select(userId.asId(), null, now) }
    }

    @Test
    fun `report returns report`() {
        val now = LocalDate.now()
        val startDate = now.minusDays(1)
        val endDate = now.plusDays(1)
        val transferReport = TransferReport(

            total = listOf(Money(Currency.EUR, 5, 0), Money(Currency.USD, -5, 0))
        )

        every { transferReportCreation.createReport(userId.asId(), startDate, endDate) } returns transferReport

        mockMvc.get("/api/v1/transfers/report?end_date=${endDate}&start_date=${startDate}")
            .andExpect {
                status { isOk() }
                jsonPath("total[0].currencyCode") { value(Currency.EUR.code) }
                jsonPath("total[1].currencyCode") { value(Currency.USD.code) }
                jsonPath("total[1].amount") { value(-5) }
            }

        verify(exactly = 1) { transferReportCreation.createReport(userId.asId(), startDate, endDate) }
    }

    @ContextConfiguration
    class TestContextConfiguration {

        @Primary
        @Bean
        fun transfersController() = TransferController(
            transferAdding,
            transferDeletion,
            transferSelection,
            transferReportCreation,
            clock
        )
    }

    private companion object {
        const val userId = 5L

        val transferAdding = mockk<TransferAdding>()
        val transferDeletion = mockk<TransferDeletion>()
        val transferSelection = mockk<TransferSelection>()
        val transferReportCreation = mockk<TransferReportCreation>()
        val clock = Clock.fixed(Instant.now(), ZoneId.of("Z"))
    }
}