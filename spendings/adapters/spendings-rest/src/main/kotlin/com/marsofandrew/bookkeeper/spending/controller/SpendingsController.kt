package com.marsofandrew.bookkeeper.spending.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.SpendingAdding
import com.marsofandrew.bookkeeper.spending.SpendingDeletion
import com.marsofandrew.bookkeeper.spending.SpendingReportCreation
import com.marsofandrew.bookkeeper.spending.SpendingSelection
import com.marsofandrew.bookkeeper.spending.controller.dto.CreateSpendingDto
import com.marsofandrew.bookkeeper.spending.controller.dto.SpendingDto
import com.marsofandrew.bookkeeper.spending.controller.dto.SpendingReportDto
import com.marsofandrew.bookkeeper.spending.controller.dto.toSpendingDto
import com.marsofandrew.bookkeeper.spending.controller.dto.toSpendingReportDto
import com.marsofandrew.bookkeeper.userContext.UserId
import java.time.Clock
import java.time.LocalDate
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
        @PathVariable("id") id: Long
    ) {
        spendingDeletion.delete(userId.asId(), setOf(id.asId()))
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
