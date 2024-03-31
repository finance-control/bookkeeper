package com.marsofandrew.bookkeeper.category.controller

import com.marsofandrew.bookkeeper.category.CategoryAdding
import com.marsofandrew.bookkeeper.category.CategoryDeletion
import com.marsofandrew.bookkeeper.category.CategorySelection
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.controller.dto.CreateTransferCategoryDto
import com.marsofandrew.bookkeeper.category.controller.dto.TransferCategoryDto
import com.marsofandrew.bookkeeper.category.controller.dto.toTransferCategoryDto
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
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
@RequestMapping("/api/v1/transfers/categories")
internal class TransferCategoriesController(
    private val transferCategoryAdding: CategoryAdding<TransferUserCategory>,
    private val transferCategoryDeletion: CategoryDeletion<TransferUserCategory>,
    private val transferCategorySelection: CategorySelection<TransferUserCategory>,
) {

    @GetMapping
    fun get(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("ids", required = false) ids: Set<Long>?
    ): List<TransferCategoryDto> {
        val categories = if (ids == null) {
            transferCategorySelection.select(userId.asId())
        } else {
            transferCategorySelection.select(userId.asId(), ids.mapTo(HashSet()) { it.asId() })
        }
        return categories.map { it.toTransferCategoryDto() }
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun create(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestBody body: CreateTransferCategoryDto
    ): TransferCategoryDto {
        return transferCategoryAdding.add(body.toTransferUserCategory(userId.asId())).toTransferCategoryDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun delete(
        @Parameter(hidden = true) @UserId userId: Long,
        @PathVariable("id") id: Long
    ) {
        transferCategoryDeletion.delete(userId.asId(), setOf(id.asId()))
    }
}
