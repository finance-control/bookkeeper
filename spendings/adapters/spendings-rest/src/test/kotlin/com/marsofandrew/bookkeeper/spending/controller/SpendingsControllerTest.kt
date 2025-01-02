package com.marsofandrew.bookkeeper.spending.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.marsofandrew.bookkeeper.base.date
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingAdding
import com.marsofandrew.bookkeeper.spending.SpendingDeletion
import com.marsofandrew.bookkeeper.spending.SpendingModification
import com.marsofandrew.bookkeeper.spending.SpendingReport
import com.marsofandrew.bookkeeper.spending.SpendingReportCreation
import com.marsofandrew.bookkeeper.spending.SpendingReportsWithCategories
import com.marsofandrew.bookkeeper.spending.SpendingSelection
import com.marsofandrew.bookkeeper.spending.SpendingWithCategory
import com.marsofandrew.bookkeeper.spending.category.Category
import com.marsofandrew.bookkeeper.spending.controller.dto.AccountBoundedMoneyDto
import com.marsofandrew.bookkeeper.spending.controller.dto.CreateSpendingDto
import com.marsofandrew.bookkeeper.spending.controller.dto.PositiveMoneyDto
import com.marsofandrew.bookkeeper.spending.controller.dto.UpdateSpendingDto
import com.marsofandrew.bookkeeper.spending.fixture.category
import com.marsofandrew.bookkeeper.spending.fixture.spending
import com.marsofandrew.bookkeeper.spending.fixture.spendingUpdate
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
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post


@WebMvcTest
@ContextConfiguration(
    classes = [
        SpendingsControllerTest.TestContextConfiguration::class,
        AuthArgumentContextConfiguration::class,
    ]
)
internal class SpendingsControllerTest {

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
    fun `add when correct values are set when creates the spending and returns id`() {
        val now = clock.date()
        val createSpendingDto = CreateSpendingDto(
            money = AccountBoundedMoneyDto(PositiveMoneyDto(200, 2, "EUR"), null),
            date = now,
            description = "",
            categoryId = 1
        )
        val spending = Spending(
            NumericId.unidentified(),
            userId.asId(),
            createSpendingDto.money.money.toPositiveMoney(),
            createSpendingDto.date,
            createSpendingDto.description,
            createSpendingDto.categoryId.asId(),
            now,
            createSpendingDto.money.accountId?.asId(),
            Version(0)
        )
        val identifiedSpending = spending.copy(id = 1100.asId())

        every { spendingAdding.add(spending) } returns
                SpendingWithCategory(identifiedSpending, category(identifiedSpending.categoryId))

        mockMvc.post("/api/v1/spendings") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createSpendingDto)
        }.andExpect {
            status { isOk() }
            jsonPath("id") { value(identifiedSpending.id.value) }
            jsonPath("userId") { value(userId) }
            jsonPath("money.money.amount") {
                value(
                    identifiedSpending.money.amount.movePointRight(
                        identifiedSpending.money.amount.scale()
                    )
                )
            }
            jsonPath("money.money.digits") { value(2) }
            jsonPath("money.money.currencyCode") { value(identifiedSpending.money.currency.name) }
            jsonPath("date") { value(now.toString()) }
        }

        verify(exactly = 1) { spendingAdding.add(spending) }
    }

    @Test
    fun `deletes deletes when provided id exists`() {
        val id = 55
        every { spendingDeletion.delete(userId.asId(), setOf(id.asId())) } returns Unit

        mockMvc.delete("/api/v1/spendings/{id}", id).andExpect {
            status { isNoContent() }
        }

        verify(exactly = 1) { spendingDeletion.delete(userId.asId(), setOf(id.asId())) }
    }

    @Test
    fun `get returns spendings`() {
        val now = LocalDate.now()
        val spendings = listOf(
            spending(1.asId(), userId.asId()),
            spending(2.asId(), userId.asId()),
            spending(3.asId(), userId.asId())
        )

        every { spendingSelection.select(userId.asId(), null, now) } returns spendings.map {
            SpendingWithCategory(it, category(it.categoryId))
        }

        mockMvc.get("/api/v1/spendings?end_date=${now}")
            .andExpect {
                status { isOk() }
                jsonPath("$") {
                    isArray()
                }
            }

        verify(exactly = 1) { spendingSelection.select(userId.asId(), null, now) }
    }

    @Test
    fun `patch modifies spending`() {
        val update = spendingUpdate(1.asId()) {
            description = "description"
        }

        val updateSpendingDto = UpdateSpendingDto(
            money = null,
            date = null,
            description = update.description,
            categoryId = null,
            version = 0
        )

        val identifiedSpending = spending(update.id, userId.asId())

        every { spendingModification.modify(userId.asId(), update) } returns
                SpendingWithCategory(identifiedSpending, category(identifiedSpending.categoryId))

        mockMvc.patch("/api/v1/spendings/${identifiedSpending.id.value}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updateSpendingDto)
        }.andExpect {
            status { isOk() }
            jsonPath("id") { value(identifiedSpending.id.value) }
            jsonPath("userId") { value(userId) }
        }

        verify(exactly = 1) { spendingModification.modify(userId.asId(), update) }
    }

    @Test
    fun `report returns report`() {
        val now = LocalDate.now()
        val startDate = now.minusDays(1)
        val endDate = now.plusDays(1)
        val spendingReport = SpendingReport(
            spendingByCategory = mapOf(
                1.asId<Category>() to listOf(PositiveMoney(Currency.EUR, 5, 0)),
                2.asId<Category>() to listOf(PositiveMoney(Currency.USD, 5, 0)),
            ),
            total = listOf(PositiveMoney(Currency.EUR, 5, 0), PositiveMoney(Currency.USD, 5, 0))
        )

        every { spendingReportCreation.createReport(userId.asId(), startDate, endDate) } returns
                SpendingReportsWithCategories(
                    report = spendingReport,
                    categories = spendingReport.spendingByCategory.keys.associate { it to category(it) }
                )

        mockMvc.get("/api/v1/spendings/report?end_date=${endDate}&start_date=${startDate}")
            .andExpect {
                status { isOk() }
                jsonPath("spendingByCategory.1[0].currencyCode") { value(Currency.EUR.name) }
                jsonPath("spendingByCategory.2[0].currencyCode") { value(Currency.USD.name) }
                jsonPath("spendingByCategory.1[0].amount") { value(5) }
                jsonPath("spendingByCategory.2[0].amount") { value(5) }
                jsonPath("spendingByCategory.1[0].digits") { value(0) }
                jsonPath("spendingByCategory.2[0].digits") { value(0) }
                jsonPath("total[0].currencyCode") { value(Currency.EUR.name) }
                jsonPath("total[1].currencyCode") { value(Currency.USD.name) }
            }

        verify(exactly = 1) { spendingReportCreation.createReport(userId.asId(), startDate, endDate) }
    }

    @ContextConfiguration
    class TestContextConfiguration {

        @Primary
        @Bean
        fun spendingsController() = SpendingsController(
            spendingAdding,
            spendingDeletion,
            spendingSelection,
            spendingModification,
            spendingReportCreation,
            clock
        )
    }

    private companion object {
        const val userId = 5L

        val spendingAdding = mockk<SpendingAdding>()
        val spendingDeletion = mockk<SpendingDeletion>()
        val spendingSelection = mockk<SpendingSelection>()
        val spendingReportCreation = mockk<SpendingReportCreation>()
        val spendingModification = mockk<SpendingModification>()
        val clock = Clock.fixed(Instant.now(), ZoneId.of("Z"))
    }
}
