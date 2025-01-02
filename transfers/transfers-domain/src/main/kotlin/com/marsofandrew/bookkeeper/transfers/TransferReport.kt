package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.category.Category

data class TransferReport(
    val total: List<Money>,
    //TODO: Add it
    //val byCategory: Map<NumericId<Category>>
)
