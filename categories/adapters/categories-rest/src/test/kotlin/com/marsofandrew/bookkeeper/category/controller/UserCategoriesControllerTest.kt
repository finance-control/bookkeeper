package com.marsofandrew.bookkeeper.category.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.marsofandrew.bookkeeper.category.*
import com.marsofandrew.bookkeeper.category.controller.dto.CreateUserCategoryDto
import com.marsofandrew.bookkeeper.category.controller.dto.toUserCategoryDto
import com.marsofandrew.bookkeeper.category.fixture.userCategory
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
import org.springframework.test.web.servlet.*

@WebMvcTest
@ContextConfiguration(
    classes = [
        UserCategoriesControllerTest.TestContextConfiguration::class,
        AuthArgumentContextConfiguration::class
    ]
)
internal class UserCategoriesControllerTest {

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
        val userCategory = userCategory(NumericId.unidentified()) {
            title = providedTitle
            userId = providedUserId.asId()
        }
        val identifiedUserCategory = userCategory.copy(id = 5.asId())

        every { categoryAdding.add(userCategory) } returns identifiedUserCategory

        mockMvc.post("/api/v1/categories") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                CreateUserCategoryDto(
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
        val userCategoryId = 458.asId<UserCategory>()

        every { categoryDeletion.delete(providedUserId.asId(), setOf(userCategoryId)) } returns Unit

        mockMvc.delete("/api/v1/categories/${userCategoryId.value}")
            .andExpect {
                status { isNoContent() }
            }

        verify(exactly = 1) { categoryDeletion.delete(providedUserId.asId(), setOf(userCategoryId)) }
    }

    @Test
    fun `get when parameters are absent returns all categories`() {
        val categories = listOf(
            userCategory(1.asId()) {
                title = "test1"
                userId = providedUserId.asId()
            },
            userCategory(2.asId()) {
                title = "test2"
                userId = providedUserId.asId()
            }
        )
        every { categorySelection.select(providedUserId.asId()) } returns categories

        mockMvc.get("/api/v1/categories")
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
            userCategory(1.asId()) {
                title = "test1"
                userId = providedUserId.asId()
            }
        )
        every { categorySelection.select(providedUserId.asId(), setOf(1.asId())) } returns categories

        mockMvc.get("/api/v1/categories?ids=1")
            .andExpect {
                status { isOk() }
                jsonPath("[0].id") { value(1) }
                jsonPath("[0].title") { value("test1") }
            }
    }

    @Test
    fun `modify when everything is ok returns modified category`() {
        val category = userCategory(1.asId()) {
            userId = providedUserId.asId()
        }

        every { categoryModification.modify(providedUserId.asId(), category) } returns category

        mockMvc.put("/api/v1/categories"){
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(category.toUserCategoryDto())
        }.andExpect {
            status { isOk() }
            jsonPath("id") { value(category.id.value) }
            jsonPath("title") { value(category.title) }
        }
    }

    @ContextConfiguration
    class TestContextConfiguration {

        @Primary
        @Bean
        fun spendingsController() = UserCategoriesController(
            categoryAdding,
            categoryDeletion,
            categorySelection,
            categoryModification
        )
    }

    private companion object {
        const val providedUserId = 5L

        val categoryAdding = mockk<CategoryAdding>()
        val categoryDeletion = mockk<CategoryDeletion>()
        val categorySelection = mockk<CategorySelection>()
        val categoryModification = mockk<CategoryModification>()
    }
}
