package com.marsofandrew.bookkeeper.categories.impl.transfer

import com.marsofandrew.bookkeeper.categories.CategoryDeletion
import com.marsofandrew.bookkeeper.categories.TransferUserCategory
import com.marsofandrew.bookkeeper.categories.access.CategoryStorage
import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class TransferCategoryDeletionImpl(
    private val transferUserCategoryStorage: CategoryStorage<TransferUserCategory>
): CategoryDeletion<TransferUserCategory> {

    override fun delete(userId: NumericId<User>, ids: Set<NumericId<TransferUserCategory>>) {
        transferUserCategoryStorage.findAllByUserIdAndIds(userId, ids)
            .mapTo(HashSet()) { it.id }
            .let { transferUserCategoryStorage.delete(ids) }
    }
}