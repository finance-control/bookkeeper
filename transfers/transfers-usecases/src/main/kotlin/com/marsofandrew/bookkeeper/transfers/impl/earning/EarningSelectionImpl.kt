package com.marsofandrew.bookkeeper.transfers.impl.earning

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.TransferWithCategory
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.category.TransferCategorySelector
import com.marsofandrew.bookkeeper.transfers.earning.EarningSelection
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransferSelector
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

class EarningSelectionImpl(
    transferStorage: TransferStorage,
    transferCategorySelector: TransferCategorySelector,
) : EarningSelection {

    private val standardTransferSelector = StandardTransferSelector(
        transferStorage,
        transferCategorySelector,
        { it.send == null }
    ) { Earning.of(it) }

    override fun select(userId: NumericId<User>, startDate: LocalDate?, endDate: LocalDate): List<TransferWithCategory<Earning>> {
        return standardTransferSelector.select(userId, startDate, endDate)
    }

}