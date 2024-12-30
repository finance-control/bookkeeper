package com.marsofandrew.bookkeeper.category.controller

import com.marsofandrew.bookkeeper.category.CategoryAdding
import com.marsofandrew.bookkeeper.category.CategoryDeletion
import com.marsofandrew.bookkeeper.category.CategoryModification
import com.marsofandrew.bookkeeper.category.CategorySelection
import com.marsofandrew.bookkeeper.category.controller.dto.CreateUserCategoryDto
import com.marsofandrew.bookkeeper.category.controller.dto.UserCategoryDto
import com.marsofandrew.bookkeeper.category.controller.dto.toUserCategory
import com.marsofandrew.bookkeeper.category.controller.dto.toUserCategoryDto
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/categories")
internal class UserCategoriesController(
    private val categoryAdding: CategoryAdding,
    private val categoryDeletion: CategoryDeletion,
    private val categorySelection: CategorySelection,
    private val categoryModification: CategoryModification
) {

    @GetMapping
    fun get(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("ids", required = false) ids: Set<Long>?,
        @RequestParam("title", required = false) title: String?,
    ): List<UserCategoryDto> {
        val categories = if (ids == null && title == null) {
            categorySelection.select(userId.asId())
        } else if (ids != null && title == null) {
            categorySelection.select(userId.asId(), ids.mapTo(HashSet()) { it.asId() })
        } else {
            listOf(categorySelection.select(userId.asId(), requireNotNull(title)))
        }
        return categories.map { it.toUserCategoryDto() }
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun create(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestBody body: CreateUserCategoryDto
    ): UserCategoryDto {
        return categoryAdding.add(body.toUserCategory(userId.asId())).toUserCategoryDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun delete(
        @Parameter(hidden = true) @UserId userId: Long,
        @PathVariable("id") id: Long
    ) {
        categoryDeletion.delete(userId.asId(), setOf(id.asId()))
    }

    @PutMapping
    fun modify(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestBody body: UserCategoryDto
    ): UserCategoryDto {
        return categoryModification.modify(userId.asId(), body.toUserCategory()).toUserCategoryDto()
    }
}
