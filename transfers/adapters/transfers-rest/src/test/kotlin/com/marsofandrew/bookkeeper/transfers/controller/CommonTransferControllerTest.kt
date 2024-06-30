package com.marsofandrew.bookkeeper.transfers.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.CommonTransferDeletion
import com.marsofandrew.bookkeeper.transfers.TransferReport
import com.marsofandrew.bookkeeper.transfers.CommonTransferReportCreation
import com.marsofandrew.bookkeeper.transfers.CommonTransferSelection
import com.marsofandrew.bookkeeper.transfers.controller.dto.AccountMoneyDto
import com.marsofandrew.bookkeeper.transfers.controller.dto.CreateTransferDto
import com.marsofandrew.bookkeeper.transfers.controller.dto.PositiveMoneyDto
import com.marsofandrew.bookkeeper.transfers.controller.dto.toAccountMoney
import com.marsofandrew.bookkeeper.transfers.fixtures.commonTransfer
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
        CommonTransferControllerTest.TestContextConfiguration::class,
        AuthArgumentContextConfiguration::class
    ]
)
internal class CommonTransferControllerTest {

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
    fun `deletes deletes when provided id exists`() {
        val id = 55
        every { commonTransferDeletion.delete(userId.asId(), setOf(id.asId())) } returns Unit

        mockMvc.delete("/api/v1/common-transfers/{id}", id).andExpect {
            status { isNoContent() }
        }

        verify(exactly = 1) { commonTransferDeletion.delete(userId.asId(), setOf(id.asId())) }
    }

    @Test
    fun `get returns transfers`() {
        val now = LocalDate.now()
        val transfers = listOf(
            commonTransfer(1.asId(), userId.asId()),
            commonTransfer(2.asId(), userId.asId()),
            commonTransfer(3.asId(), userId.asId())
        )

        every { commonTransferSelection.select(userId.asId(), null, now) } returns transfers

        mockMvc.get("/api/v1/common-transfers?end_date=${now}")
            .andExpect {
                status { isOk() }
                jsonPath("$") {
                    isArray()
                }
            }

        verify(exactly = 1) { commonTransferSelection.select(userId.asId(), null, now) }
    }

    @Test
    fun `report returns report`() {
        val now = LocalDate.now()
        val startDate = now.minusDays(1)
        val endDate = now.plusDays(1)
        val transferReport = TransferReport(

            total = listOf(Money(Currency.EUR, 5, 0), Money(Currency.USD, -5, 0))
        )

        every { commonTransferReportCreation.createReport(userId.asId(), startDate, endDate) } returns transferReport

        mockMvc.get("/api/v1/common-transfers/report?end_date=${endDate}&start_date=${startDate}")
            .andExpect {
                status { isOk() }
                jsonPath("total[0].currencyCode") { value(Currency.EUR.name) }
                jsonPath("total[1].currencyCode") { value(Currency.USD.name) }
                jsonPath("total[1].amount") { value(-5) }
            }

        verify(exactly = 1) { commonTransferReportCreation.createReport(userId.asId(), startDate, endDate) }
    }

    @ContextConfiguration
    class TestContextConfiguration {

        @Primary
        @Bean
        fun transfersController() = CommonTransferController(
            commonTransferDeletion,
            commonTransferSelection,
            commonTransferReportCreation
        )
    }

    private companion object {
        const val userId = 5L

        val commonTransferDeletion = mockk<CommonTransferDeletion>()
        val commonTransferSelection = mockk<CommonTransferSelection>()
        val commonTransferReportCreation = mockk<CommonTransferReportCreation>()
        val clock = Clock.fixed(Instant.now(), ZoneId.of("Z"))
    }
}