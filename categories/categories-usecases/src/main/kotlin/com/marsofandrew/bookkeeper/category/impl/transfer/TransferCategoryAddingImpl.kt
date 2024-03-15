package com.marsofandrew.bookkeeper.category.impl.transfer

import com.marsofandrew.bookkeeper.category.CategoryAdding
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage

class TransferCategoryAddingImpl(
    private val transferUserCategoryStorage: CategoryStorage<TransferUserCategory>
) : CategoryAdding<TransferUserCategory> {

    override fun add(category: TransferUserCategory): TransferUserCategory {
        return transferUserCategoryStorage.create(category)
    }
}
