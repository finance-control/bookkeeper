package com.marsofandrew.bookkeeper.category.impl

import com.marsofandrew.bookkeeper.category.CategoryDeletion
import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.access.CategoryStorage
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import org.apache.logging.log4j.LogManager

class CategoryDeletionImpl(
    private val categoryStorage: CategoryStorage
) : CategoryDeletion {

    private val logger = LogManager.getLogger()

    override fun delete(userId: NumericId<User>, ids: Set<NumericId<UserCategory>>) {
        categoryStorage.findAllByUserIdAndIds(userId, ids)
            .mapTo(HashSet()) { it.id }
            .let {
                categoryStorage.delete(it)
                logger.info("UserCategories with ids $it were deleted")
            }
    }
}
