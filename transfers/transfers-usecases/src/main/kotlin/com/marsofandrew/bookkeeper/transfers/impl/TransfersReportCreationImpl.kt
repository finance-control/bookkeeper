package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.transfers.TransferReportCreation
import com.marsofandrew.bookkeeper.transfers.TransferReport
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.user.User
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.NumericId
import java.math.BigDecimal
import java.time.LocalDate

class TransfersReportCreationImpl(
    private val transferStorage: TransferStorage
) : TransferReportCreation {

    override fun createReport(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): TransferReport {
        if (startDate > endDate) {
            throw IllegalArgumentException("Start date om more than end date")
        }

        val transfersByCurrency: MutableMap<Currency, BigDecimal> = mutableMapOf()

        transferStorage.findAllByUserIdBetween(userId, startDate, endDate)
            .forEach { transfer ->
                transfersByCurrency[transfer.received.currency] =
                    transfersByCurrency.getOrDefault(transfer.received.currency, BigDecimal.ZERO)
                        .plus(transfer.received.amount)

                if (transfer.send != null) {
                    val send = requireNotNull(transfer.send)
                    transfersByCurrency[send.currency] =
                        transfersByCurrency.getOrDefault(send.currency, BigDecimal.ZERO)
                            .minus(send.amount)
                }

                if (transfer.fee != null) {
                    val fee = requireNotNull(transfer.fee)
                    transfersByCurrency[fee.currency] =
                        transfersByCurrency.getOrDefault(fee.currency, BigDecimal.ZERO)
                            .minus(fee.amount)
                }
            }

        return TransferReport(
            total = transfersByCurrency.map { (currency, amount) -> Money(currency, amount) }
        )
    }
}