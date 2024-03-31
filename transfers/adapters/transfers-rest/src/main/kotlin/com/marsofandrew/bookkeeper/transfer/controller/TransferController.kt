package com.marsofandrew.bookkeeper.transfer.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.TransferAdding
import com.marsofandrew.bookkeeper.transfer.TransferDeletion
import com.marsofandrew.bookkeeper.transfer.TransferReportCreation
import com.marsofandrew.bookkeeper.transfer.TransferSelection
import com.marsofandrew.bookkeeper.transfer.controller.dto.*
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.Clock
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/transfers")
internal class TransferController(
    private val transferAdding: TransferAdding,
    private val transferDeletion: TransferDeletion,
    private val transferSelection: TransferSelection,
    private val transferReportCreation: TransferReportCreation,
    private val clock: Clock,
) {

    @PostMapping
    fun create(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestBody(required = true) body: CreateTransferDto,
    ): TransferDto {
        return transferAdding.add(body.toSpending(userId.asId(), clock)).toTransferDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun delete(
        @Parameter(hidden = true) @UserId userId: Long,
        @PathVariable("id") id: Long
    ) {
        transferDeletion.delete(userId.asId(), setOf(id.asId()))
    }

    @GetMapping
    fun get(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("start_date", required = false) startDate: LocalDate?,
        @RequestParam("end_date", required = false) endDate: LocalDate?,
    ): List<TransferDto> {

        val transfers = if (endDate == null) {
            transferSelection.select(userId.asId(), startDate)
        } else {
            transferSelection.select(userId.asId(), startDate, endDate)
        }

        return transfers.map { it.toTransferDto() }
    }

    @GetMapping("/report")
    fun getReport(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("start_date") startDate: LocalDate,
        @RequestParam("end_date") endDate: LocalDate,
    ): TransferReportDto {
        return transferReportCreation.createReport(userId.asId(), startDate, endDate).toTransferReportDto()
    }
}
