package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.TransferReport
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.category.Category
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
        val totalByCategory: MutableMap<NumericId<Category>, MutableMap<Currency, BigDecimal>> = mutableMapOf()

        transferStorage.findAllByUserIdBetween(userId, startDate, endDate)
            .filter(filter)
            .forEach { transfer ->
                totalByCategory[transfer.categoryId] = totalByCategory.getOrDefault(transfer.categoryId, mutableMapOf())

                transfersByCurrency.addMoney(transfer.received.money)
                totalByCategory[transfer.categoryId]!!.addMoney(transfer.received.money)

                if (transfer.send != null) {
                    val accountsMoney = requireNotNull(transfer.send?.money)

                    transfersByCurrency.subtractMoney(accountsMoney)
                    totalByCategory[transfer.categoryId]!!.subtractMoney(accountsMoney)
                }
            }

        return TransferReport(
            total = transfersByCurrency.toMoneys(),
            byCategory = totalByCategory.map { (categoryId, moneys) -> categoryId to moneys.toMoneys() }
                .toMap()
        )
    }
}

private fun MutableMap<Currency, BigDecimal>.addMoney(money: PositiveMoney) {
    this[money.currency] = getOrDefault(money.currency, BigDecimal.ZERO) + money.amount
}

private fun MutableMap<Currency, BigDecimal>.subtractMoney(money: PositiveMoney) {
    this[money.currency] = getOrDefault(money.currency, BigDecimal.ZERO) - money.amount
}

private fun Map<Currency, BigDecimal>.toMoneys(): List<Money> =
    map { (currency, amount) -> Money(currency, amount) }