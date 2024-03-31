package com.marsofandrew.bookkeeper.category.controller

import com.marsofandrew.bookkeeper.category.CategoryAdding
import com.marsofandrew.bookkeeper.category.CategoryDeletion
import com.marsofandrew.bookkeeper.category.CategorySelection
import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.controller.dto.CreateSpendingCategoryDto
import com.marsofandrew.bookkeeper.category.controller.dto.SpendingCategoryDto
import com.marsofandrew.bookkeeper.category.controller.dto.toSpendingCategoryDto
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/spendings/categories")
internal class SpendingCategoriesController(
    private val spendingCategoryAdding: CategoryAdding<SpendingUserCategory>,
    private val spendingCategoryDeletion: CategoryDeletion<SpendingUserCategory>,
    private val spendingCategorySelection: CategorySelection<SpendingUserCategory>,
) {

    @GetMapping
    fun get(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("ids", required = false) ids: Set<Long>?
    ): List<SpendingCategoryDto> {
        val categories = if (ids == null) {
            spendingCategorySelection.select(userId.asId())
        } else {
            spendingCategorySelection.select(userId.asId(), ids.mapTo(HashSet()) { it.asId() })
        }
        return categories.map { it.toSpendingCategoryDto() }
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun create(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestBody body: CreateSpendingCategoryDto
    ): SpendingCategoryDto {
        return spendingCategoryAdding.add(body.toSpendingCategory(userId.asId())).toSpendingCategoryDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun delete(
        @Parameter(hidden = true) @UserId userId: Long,
        @PathVariable("id") id: Long
    ) {
        spendingCategoryDeletion.delete(userId.asId(), setOf(id.asId()))
    }
}
