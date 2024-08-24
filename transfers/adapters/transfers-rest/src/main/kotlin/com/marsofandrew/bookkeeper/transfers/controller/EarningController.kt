package com.marsofandrew.bookkeeper.transfers.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.controller.dto.*
import com.marsofandrew.bookkeeper.transfers.earning.EarningAdding
import com.marsofandrew.bookkeeper.transfers.earning.EarningModification
import com.marsofandrew.bookkeeper.transfers.earning.EarningReportCreation
import com.marsofandrew.bookkeeper.transfers.earning.EarningSelection
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.*
import java.time.Clock
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/earnings")
internal class EarningController(
    private val earningAdding: EarningAdding,
    private val earningSelection: EarningSelection,
    private val earningReportCreation: EarningReportCreation,
    private val earningModification: EarningModification,
    private val clock: Clock,
) {

    @PostMapping
    fun create(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestBody(required = true) body: CreateEarningDto,
    ): TransferDto {
        return earningAdding.add(body.toEarning(userId.asId(), clock)).toTransferDto()
    }

    @GetMapping
    fun get(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("start_date", required = false) startDate: LocalDate?,
        @RequestParam("end_date", required = false) endDate: LocalDate?,
    ): List<TransferDto> {

        val transfers = if (endDate == null) {
            earningSelection.select(userId.asId(), startDate)
        } else {
            earningSelection.select(userId.asId(), startDate, endDate)
        }

        return transfers.map { it.toTransferDto() }
    }

    @PatchMapping("{id}")
    fun update(
        @Parameter(hidden = true) @UserId userId: Long,
        @PathVariable("id") id: Long,
        @RequestBody(required = true) body: UpdateEarningDto,
    ): TransferDto {
        return earningModification.modify(userId.asId(), body.toEarningUpdate(id.asId())).toTransferDto()
    }

    @GetMapping("/report")
    fun getReport(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("start_date") startDate: LocalDate,
        @RequestParam("end_date") endDate: LocalDate,
    ): TransferReportDto {
        return earningReportCreation.createReport(userId.asId(), startDate, endDate).toTransferReportDto()
    }
}
