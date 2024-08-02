package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.TransferReport
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.impl.utils.validateDates
import com.marsofandrew.bookkeeper.transfers.user.User
import java.math.BigDecimal
import java.time.LocalDate

internal class StandardTransfersReportCreator(
    private val transferStorage: TransferStorage,
    private val filter: (CommonTransferBase) -> Boolean,
) {

    fun createReport(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): TransferReport {
        validateDates(startDate, endDate)

        val transfersByCurrency: MutableMap<Currency, BigDecimal> = mutableMapOf()

        transferStorage.findAllByUserIdBetween(userId, startDate, endDate)
            .filter(filter)
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