package com.marsofandrew.bookkeeper.spendings.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spendings.SpendingAdding
import com.marsofandrew.bookkeeper.spendings.SpendingDeletion
import com.marsofandrew.bookkeeper.spendings.SpendingReportCreation
import com.marsofandrew.bookkeeper.spendings.SpendingSelection
import com.marsofandrew.bookkeeper.spendings.controller.dto.*
import com.marsofandrew.bookkeeper.userContext.UserId
import java.time.Clock
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/spendings")
internal class SpendingsController(
    private val spendingAdding: SpendingAdding,
    private val spendingDeletion: SpendingDeletion,
    private val spendingSelection: SpendingSelection,
    private val spendingReportCreation: SpendingReportCreation,
    private val clock: Clock
) {

    @PostMapping
    fun create(
        @UserId userId: Long,
        @RequestBody(required = true) body: CreateSpendingDto,
    ): SpendingDto {
        return spendingAdding.add(body.toSpending(userId.asId(), clock)).toSpendingDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun delete(
        @UserId userId: Long,
        @PathVariable("id") id: String
    ) {
        spendingDeletion.delete(setOf(id.asId()))
    }

    @GetMapping
    fun get(
        @UserId userId: Long,
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
        @UserId userId: Long,
        @RequestParam("start_date") startDate: LocalDate,
        @RequestParam("end_date") endDate: LocalDate,
    ): SpendingReportDto {
        return spendingReportCreation.createReport(userId.asId(), startDate, endDate).toSpendingReportDto()
    }
}