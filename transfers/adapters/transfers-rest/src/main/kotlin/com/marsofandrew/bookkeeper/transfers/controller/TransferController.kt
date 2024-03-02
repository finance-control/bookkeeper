package com.marsofandrew.bookkeeper.transfers.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.TransferAdding
import com.marsofandrew.bookkeeper.transfers.TransferDeletion
import com.marsofandrew.bookkeeper.transfers.TransferReportCreation
import com.marsofandrew.bookkeeper.transfers.TransferSelection
import com.marsofandrew.bookkeeper.transfers.controller.dto.CreateTransferDto
import com.marsofandrew.bookkeeper.transfers.controller.dto.TransferDto
import com.marsofandrew.bookkeeper.transfers.controller.dto.TransferReportDto
import com.marsofandrew.bookkeeper.transfers.controller.dto.toTransferDto
import com.marsofandrew.bookkeeper.transfers.controller.dto.toTransferReportDto
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
        @UserId userId: Long,
        @RequestBody(required = true) body: CreateTransferDto,
    ): TransferDto {
        return transferAdding.add(body.toSpending(userId.asId(), clock)).toTransferDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun delete(
        @UserId userId: Long,
        @PathVariable("id") id: Long
    ) {
        transferDeletion.delete(userId.asId(), setOf(id.asId()))
    }

    @GetMapping
    fun get(
        @UserId userId: Long,
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
        @UserId userId: Long,
        @RequestParam("start_date") startDate: LocalDate,
        @RequestParam("end_date") endDate: LocalDate,
    ): TransferReportDto {
        return transferReportCreation.createReport(userId.asId(), startDate, endDate).toTransferReportDto()
    }
}