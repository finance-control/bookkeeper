package com.marsofandrew.bookkeeper.categories.impl.transfer

import com.marsofandrew.bookkeeper.categories.CategorySelection
import com.marsofandrew.bookkeeper.categories.TransferUserCategory
import com.marsofandrew.bookkeeper.categories.access.CategoryStorage
import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class TransferCategorySelectionImpl(
    private val transferUserCategoryStorage: CategoryStorage<TransferUserCategory>
) : CategorySelection<TransferUserCategory> {
    override fun select(
        userId: NumericId<User>,
        ids: Set<NumericId<TransferUserCategory>>
    ): List<TransferUserCategory> {
        return transferUserCategoryStorage.findAllByUserIdAndIds(userId, ids)
    }

    override fun select(userId: NumericId<User>): List<TransferUserCategory> {
        return transferUserCategoryStorage.findAllByUserId(userId)
    }
}