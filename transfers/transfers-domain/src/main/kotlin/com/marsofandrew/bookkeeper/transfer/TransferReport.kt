package com.marsofandrew.bookkeeper.transfer

import com.marsofandrew.bookkeeper.properties.Money

data class TransferReport(
    val total: List<Money>
)
