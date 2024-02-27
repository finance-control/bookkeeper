package com.marsofandrew.bookkeeper.categories.impl.transfer

import com.marsofandrew.bookkeeper.categories.CategoryAdding
import com.marsofandrew.bookkeeper.categories.TransferUserCategory
import com.marsofandrew.bookkeeper.categories.access.CategoryStorage

class TransferCategoryAddingImpl(
    private val transferUserCategoryStorage: CategoryStorage<TransferUserCategory>
) : CategoryAdding<TransferUserCategory> {
    override fun add(category: TransferUserCategory): TransferUserCategory {
        return transferUserCategoryStorage.create(category)
    }
}