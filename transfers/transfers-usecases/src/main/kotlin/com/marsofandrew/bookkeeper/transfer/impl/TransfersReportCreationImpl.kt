package com.marsofandrew.bookkeeper.transfer.impl

import com.marsofandrew.bookkeeper.transfer.TransferReportCreation
import com.marsofandrew.bookkeeper.transfer.TransferReport
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.user.User
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
                transfersByCurrency[transfer.received.money.currency] =
                    transfersByCurrency.getOrDefault(transfer.received.money.currency, BigDecimal.ZERO)
                        .plus(transfer.received.money.amount)

                if (transfer.send != null) {
                    val accountsMoney = requireNotNull(transfer.send?.money)
                    transfersByCurrency[accountsMoney.currency] =
                        transfersByCurrency.getOrDefault(accountsMoney.currency, BigDecimal.ZERO)
                            .minus(accountsMoney.amount)
                }
            }

        return TransferReport(
            total = transfersByCurrency.map { (currency, amount) -> Money(currency, amount) }
        )
    }
}