package com.marsofandrew.bookkeeper.category.impl.transfer

import com.marsofandrew.bookkeeper.category.CategoryDeletion
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class TransferCategoryDeletionImpl(
    private val transferUserCategoryStorage: CategoryStorage<TransferUserCategory>
): CategoryDeletion<TransferUserCategory> {

    override fun delete(userId: NumericId<User>, ids: Set<NumericId<TransferUserCategory>>) {
        transferUserCategoryStorage.findAllByUserIdAndIds(userId, ids)
            .mapTo(HashSet()) { it.id }
            .let { transferUserCategoryStorage.delete(it) }
    }
}
