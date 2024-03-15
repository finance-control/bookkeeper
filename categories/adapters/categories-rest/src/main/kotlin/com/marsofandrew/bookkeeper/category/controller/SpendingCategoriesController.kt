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
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/spendings/categories")
internal class SpendingCategoriesController(
    private val spendingCategoryAdding: CategoryAdding<SpendingUserCategory>,
    private val spendingCategoryDeletion: CategoryDeletion<SpendingUserCategory>,
    private val spendingCategorySelection: CategorySelection<SpendingUserCategory>,
) {

    @GetMapping
    fun get(
        @UserId userId: Long,
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
        @UserId userId: Long,
        @RequestBody body: CreateSpendingCategoryDto
    ): SpendingCategoryDto {
        return spendingCategoryAdding.add(body.toSpendingCategory(userId.asId())).toSpendingCategoryDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun delete(
        @UserId userId: Long,
        @PathVariable("id") id: Long
    ) {
        spendingCategoryDeletion.delete(userId.asId(), setOf(id.asId()))
    }
}
