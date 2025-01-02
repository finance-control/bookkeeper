package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.transfers.category.Category

data class TransferWithCategory<Type : CommonTransferBase>(
    val transfer: Type,
    val category: Category,
)
