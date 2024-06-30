package com.marsofandrew.bookkeeper.account.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.marsofandrew.bookkeeper.account.AccountCreation
import com.marsofandrew.bookkeeper.account.AccountDeletion
import com.marsofandrew.bookkeeper.account.AccountSelection
import com.marsofandrew.bookkeeper.account.controller.dto.CreateAccountDto
import com.marsofandrew.bookkeeper.account.controller.dto.toMoneyDto
import com.marsofandrew.bookkeeper.account.fixtures.account
import com.marsofandrew.bookkeeper.base.date
import com.marsofandrew.bookkeeper.controller.BaseExceptionHandler
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.userContext.AuthArgumentContextConfiguration
import com.marsofandrew.bookkeeper.userContext.UserIdToken
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest
@ContextConfiguration(
    classes = [
        AccountsControllerTest.ContextConfiguration::class,
        AuthArgumentContextConfiguration::class,
        BaseExceptionHandler::class
    ]
)
internal class AccountsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        clearAllMocks()
        SecurityContextHolder.getContext().authentication = UserIdToken(USER_ID)
    }

    @Test
    fun `post when correct values are provided creates account`() {
        val accOpenedAt = clock.date().minusDays(2)
        val account = account(StringId.unidentified(), USER_ID.asId()) {
            openedAt = accOpenedAt
        }
        val identifiedAccount = account.copy(id = "test".asId())
        every { accountCreation.create(account) } returns identifiedAccount

        mockMvc.post("/api/v1/accounts") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                CreateAccountDto(
                    money = account.money.toMoneyDto(),
                    title = account.title,
                    openedAt = accOpenedAt
                )
            )
        }.andExpect {
            status { isCreated() }
            jsonPath("id") { value(identifiedAccount.id.value) }
            jsonPath("money.amount") {
                value(identifiedAccount.money.amount.movePointRight(identifiedAccount.money.amount.scale()))
            }
            jsonPath("money.digits") { value(identifiedAccount.money.amount.scale()) }
            jsonPath("money.currencyCode") { value(identifiedAccount.money.currency.name) }
            jsonPath("userId") { value(USER_ID) }
        }
    }

    @Test
    fun `post when incorrect body is provided returns bad request`() {
        mockMvc.post("/api/v1/accounts") {
            contentType = MediaType.APPLICATION_JSON
            content = "{}"
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `close when id is provided closes provided account`() {
        val accountId = "test"
        mockMvc.post("/api/v1/accounts/$accountId/close")
            .andExpect {
                status { isOk() }
            }

        verify(exactly = 1) { accountDeletion.delete(USER_ID.asId(), setOf(accountId.asId())) }
    }

    @Test
    fun `get returns all accounts`() {
        val accOpenedAt = clock.date().minusDays(2)
        val account = account(StringId.unidentified(), USER_ID.asId()) {
            openedAt = accOpenedAt
        }
        val identifiedAccount = account.copy(id = "test".asId())
        every { accountSelection.select(USER_ID.asId()) } returns setOf(identifiedAccount)

        mockMvc.get("/api/v1/accounts")
            .andExpect {
                status { isOk() }
                jsonPath("[0].id") { value(identifiedAccount.id.value) }
                jsonPath("[0].money.amount") {
                    value(identifiedAccount.money.amount.movePointRight(identifiedAccount.money.amount.scale()))
                }
                jsonPath("[0].money.digits") { value(identifiedAccount.money.amount.scale()) }
                jsonPath("[0].money.currencyCode") { value(identifiedAccount.money.currency.name) }
                jsonPath("[0].userId") { value(USER_ID) }
            }
    }

    class ContextConfiguration {

        @Bean
        fun accountsController(): AccountsController = AccountsController(
            accountSelection,
            accountDeletion,
            accountCreation,
            clock
        )
    }

    companion object {
        const val USER_ID = 4587L
        val clock = Clock.fixed(Instant.now(), ZoneId.of("Z"))
        val accountSelection = mockk<AccountSelection>(relaxUnitFun = true)
        val accountDeletion = mockk<AccountDeletion>(relaxUnitFun = true)
        val accountCreation = mockk<AccountCreation>(relaxUnitFun = true)
    }
}