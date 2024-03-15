package com.marsofandrew.bookkeeper.category.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.marsofandrew.bookkeeper.category.CategoryAdding
import com.marsofandrew.bookkeeper.category.CategoryDeletion
import com.marsofandrew.bookkeeper.category.CategorySelection
import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.controller.dto.CreateSpendingCategoryDto
import com.marsofandrew.bookkeeper.category.fixture.spendingUserCategory
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.userContext.AuthArgumentContextConfiguration
import com.marsofandrew.bookkeeper.userContext.UserIdToken
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
        SpendingCategoriesControllerTest.TestContextConfiguration::class,
        AuthArgumentContextConfiguration::class
    ]
)
internal class SpendingCategoriesControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        clearAllMocks()
        SecurityContextHolder.getContext().authentication = UserIdToken(providedUserId)
    }

    @Test
    fun `post when correct data is provided creates new userCategory assign to the user`() {
        val providedTitle = "title"
        val userCategory = spendingUserCategory(NumericId.unidentified()) {
            title = providedTitle
            userId = providedUserId.asId()
        }
        val identifiedUserCategory = userCategory.copy(id = 5.asId())

        every { spendingCategoryAdding.add(userCategory) } returns identifiedUserCategory

        mockMvc.post("/api/v1/spendings/categories") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                CreateSpendingCategoryDto(
                    title = providedTitle,
                    description = null
                )
            )
        }.andExpect {
            status { isCreated() }
            jsonPath("id") { value(identifiedUserCategory.id.value) }
            jsonPath("title") { value(identifiedUserCategory.title) }
        }
    }

    @Test
    fun `delete deletes category`() {
        val userCategoryId = 458.asId<SpendingUserCategory>()

        every { spendingCategoryDeletion.delete(providedUserId.asId(), setOf(userCategoryId)) } returns Unit

        mockMvc.delete("/api/v1/spendings/categories/${userCategoryId.value}")
            .andExpect {
                status { isNoContent() }
            }

        verify(exactly = 1) { spendingCategoryDeletion.delete(providedUserId.asId(), setOf(userCategoryId)) }
    }

    @Test
    fun `get when parameters are absent returns all categories`() {
        val categories = listOf(
            spendingUserCategory(1.asId()) {
                title = "test1"
                userId = providedUserId.asId()
            },
            spendingUserCategory(2.asId()) {
                title = "test2"
                userId = providedUserId.asId()
            }
        )
        every { spendingCategorySelection.select(providedUserId.asId()) } returns categories

        mockMvc.get("/api/v1/spendings/categories")
            .andExpect {
                status { isOk() }
                jsonPath("[0].id") { value(1) }
                jsonPath("[1].id") { value(2) }
                jsonPath("[0].title") { value("test1") }
                jsonPath("[1].title") { value("test2") }
            }
    }

    @Test
    fun `get when parameters contains ids returns categories with provided ids`() {
        val categories = listOf(
            spendingUserCategory(1.asId()) {
                title = "test1"
                userId = providedUserId.asId()
            }
        )
        every { spendingCategorySelection.select(providedUserId.asId(), setOf(1.asId())) } returns categories

        mockMvc.get("/api/v1/spendings/categories?ids=1")
            .andExpect {
                status { isOk() }
                jsonPath("[0].id") { value(1) }
                jsonPath("[0].title") { value("test1") }
            }
    }

    @ContextConfiguration
    class TestContextConfiguration {

        @Primary
        @Bean
        fun spendingsController() = SpendingCategoriesController(
            spendingCategoryAdding,
            spendingCategoryDeletion,
            spendingCategorySelection
        )
    }

    private companion object {
        const val providedUserId = 5L

        val spendingCategoryAdding = mockk<CategoryAdding<SpendingUserCategory>>()
        val spendingCategoryDeletion = mockk<CategoryDeletion<SpendingUserCategory>>()
        val spendingCategorySelection = mockk<CategorySelection<SpendingUserCategory>>()
    }
}
