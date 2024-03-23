package com.marsofandrew.bookkeeper.category.impl.transfer

import com.marsofandrew.bookkeeper.category.CategoryDeletion
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import org.apache.logging.log4j.LogManager

class TransferCategoryDeletionImpl(
    private val transferUserCategoryStorage: CategoryStorage<TransferUserCategory>
) : CategoryDeletion<TransferUserCategory> {

    private val logger = LogManager.getLogger()

    override fun delete(userId: NumericId<User>, ids: Set<NumericId<TransferUserCategory>>) {
        transferUserCategoryStorage.findAllByUserIdAndIds(userId, ids)
            .mapTo(HashSet()) { it.id }
            .let {
                transferUserCategoryStorage.delete(it)
                logger.info("TransferUserCategories with ids $it were deleted")
            }
    }
}
