package com.marsofandrew.bookkeeper.spending.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.*
import com.marsofandrew.bookkeeper.spending.controller.dto.*
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.Clock
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/spendings")
internal class SpendingsController(
    private val spendingAdding: SpendingAdding,
    private val spendingDeletion: SpendingDeletion,
    private val spendingSelection: SpendingSelection,
    private val spendingModification: SpendingModification,
    private val spendingReportCreation: SpendingReportCreation,
    private val clock: Clock
) {

    @PostMapping
    fun create(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestBody(required = true) body: CreateSpendingDto,
    ): SpendingDto {
        return spendingAdding.add(body.toSpending(userId.asId(), clock)).toSpendingDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun delete(
        @Parameter(hidden = true) @UserId userId: Long,
        @PathVariable("id") id: Long
    ) {
        spendingDeletion.delete(userId.asId(), setOf(id.asId()))
    }

    @PatchMapping("/{id}")
    fun patch(
        @Parameter(hidden = true) @UserId userId: Long,
        @PathVariable("id") id: Long,
        @RequestBody body: UpdateSpendingDto,
    ): SpendingDto {
        return spendingModification.modify(userId.asId(), body.toSpendingUpdate(id.asId())).toSpendingDto()
    }

    @GetMapping
    fun get(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("start_date", required = false) startDate: LocalDate?,
        @RequestParam("end_date", required = false) endDate: LocalDate?,
    ): List<SpendingDto> {

        val spendings = if (endDate == null) {
            spendingSelection.select(userId.asId(), startDate)
        } else {
            spendingSelection.select(userId.asId(), startDate, endDate)
        }

        return spendings.map { it.toSpendingDto() }
    }

    @GetMapping("/report")
    fun getReport(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("start_date") startDate: LocalDate,
        @RequestParam("end_date") endDate: LocalDate,
    ): SpendingReportDto {
        return spendingReportCreation.createReport(userId.asId(), startDate, endDate).toSpendingReportDto()
    }
}
