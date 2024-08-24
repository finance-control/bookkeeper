package com.marsofandrew.bookkeeper.transfers.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.controller.dto.*
import com.marsofandrew.bookkeeper.transfers.transfer.TransferAdding
import com.marsofandrew.bookkeeper.transfers.transfer.TransferModification
import com.marsofandrew.bookkeeper.transfers.transfer.TransferReportCreation
import com.marsofandrew.bookkeeper.transfers.transfer.TransferSelection
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.*
import java.time.Clock
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/transfers")
internal class TransferController(
    private val transferAdding: TransferAdding,
    private val transferSelection: TransferSelection,
    private val transferModification: TransferModification,
    private val transferReportCreation: TransferReportCreation,
    private val clock: Clock,
) {

    @PostMapping
    fun create(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestBody(required = true) body: CreateTransferDto,
    ): TransferDto {
        return transferAdding.add(body.toTransfer(userId.asId(), clock)).toTransferDto()
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

    @PatchMapping("{id}")
    fun update(
        @Parameter(hidden = true) @UserId userId: Long,
        @PathVariable("id") id: Long,
        @RequestBody(required = true) body: UpdateTransferDto,
    ): TransferDto {
        return transferModification.modify(userId.asId(), body.toTransferUpdate(id.asId())).toTransferDto()
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
