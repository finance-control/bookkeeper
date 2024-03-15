package com.marsofandrew.bookkeeper.category.impl.transfer

import com.marsofandrew.bookkeeper.category.CategorySelection
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
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
