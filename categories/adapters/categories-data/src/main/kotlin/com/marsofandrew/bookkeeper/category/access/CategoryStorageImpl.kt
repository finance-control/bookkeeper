package com.marsofandrew.bookkeeper.category.access

import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.access.entity.toSpendingUserCategoryEntity
import com.marsofandrew.bookkeeper.category.access.repository.UserCategoryRepository
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import org.springframework.stereotype.Service

@Service
internal class CategoryStorageImpl(
    private val userCategoryRepository: UserCategoryRepository
) : CategoryStorage {


    override fun findAllByUserId(userId: NumericId<User>): List<UserCategory> {
        return userCategoryRepository.findAllByUserId(userId.value).map { it.toModel() }
    }

    override fun existsByUserIdAndCategoryId(userId: NumericId<User>, id: NumericId<UserCategory>): Boolean {
        return userCategoryRepository.existsByIdAndUserId(id.value, userId.value)
    }

    override fun findAllByUserIdAndIds(
        userId: NumericId<User>,
        ids: Set<NumericId<UserCategory>>
    ): List<UserCategory> {
        return userCategoryRepository.findAllByUserIdAndIdIn(userId.value, ids.mapTo(HashSet()) { it.value })
            .map { it.toModel() }
    }

    override fun delete(ids: Set<NumericId<UserCategory>>) {
        userCategoryRepository.deleteAllById(ids.map { it.value })
    }

    override fun create(userCategory: UserCategory): UserCategory {
        return userCategoryRepository.saveAndFlush(userCategory.toSpendingUserCategoryEntity()).toModel()
    }
}