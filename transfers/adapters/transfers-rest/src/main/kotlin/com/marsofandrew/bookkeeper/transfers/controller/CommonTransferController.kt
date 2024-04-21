package com.marsofandrew.bookkeeper.transfers.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.CommonTransferDeletion
import com.marsofandrew.bookkeeper.transfers.CommonTransferReportCreation
import com.marsofandrew.bookkeeper.transfers.CommonTransferSelection
import com.marsofandrew.bookkeeper.transfers.controller.dto.*
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/common-transfers")
internal class CommonTransferController(
    private val commonTransferDeletion: CommonTransferDeletion,
    private val commonTransferSelection: CommonTransferSelection,
    private val commonTransferReportCreation: CommonTransferReportCreation,
) {

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun delete(
        @Parameter(hidden = true) @UserId userId: Long,
        @PathVariable("id") id: Long
    ) {
        commonTransferDeletion.delete(userId.asId(), setOf(id.asId()))
    }

    @GetMapping
    fun get(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("start_date", required = false) startDate: LocalDate?,
        @RequestParam("end_date", required = false) endDate: LocalDate?,
    ): List<TransferDto> {

        val transfers = if (endDate == null) {
            commonTransferSelection.select(userId.asId(), startDate)
        } else {
            commonTransferSelection.select(userId.asId(), startDate, endDate)
        }

        return transfers.map { it.toTransferDto() }
    }

    @GetMapping("/report")
    fun getReport(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("start_date") startDate: LocalDate,
        @RequestParam("end_date") endDate: LocalDate,
    ): TransferReportDto {
        return commonTransferReportCreation.createReport(userId.asId(), startDate, endDate).toTransferReportDto()
    }
}
