package com.marsofandrew.bookkeeper.category.access

import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.access.entity.toSpendingUserCategoryEntity
import com.marsofandrew.bookkeeper.category.access.repository.SpendingUserCategoryRepository
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import org.springframework.stereotype.Service

@Service
internal class SpendingCategoryStorage(
    private val spendingUserCategoryRepository: SpendingUserCategoryRepository
) : CategoryStorage<SpendingUserCategory> {


    override fun findAllByUserId(userId: NumericId<User>): List<SpendingUserCategory> {
        return spendingUserCategoryRepository.findAllByUserId(userId.value).map { it.toModel() }
    }

    override fun existsByUserIdAndCategoryId(userId: NumericId<User>, id: NumericId<SpendingUserCategory>): Boolean {
        return spendingUserCategoryRepository.existsByIdAndUserId(id.value, userId.value)
    }

    override fun findAllByUserIdAndIds(
        userId: NumericId<User>,
        ids: Set<NumericId<SpendingUserCategory>>
    ): List<SpendingUserCategory> {
        return spendingUserCategoryRepository.findAllByUserIdAndIdIn(userId.value, ids.mapTo(HashSet()) { it.value })
            .map { it.toModel() }
    }

    override fun delete(ids: Set<NumericId<SpendingUserCategory>>) {
        spendingUserCategoryRepository.deleteAllById(ids.map { it.value })
    }

    override fun create(userCategory: SpendingUserCategory): SpendingUserCategory {
        return spendingUserCategoryRepository.saveAndFlush(userCategory.toSpendingUserCategoryEntity()).toModel()
    }
}
