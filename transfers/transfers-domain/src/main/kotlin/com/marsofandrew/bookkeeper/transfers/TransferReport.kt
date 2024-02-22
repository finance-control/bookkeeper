package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.properties.Money

data class TransferReport(
    val total: List<Money>
)
