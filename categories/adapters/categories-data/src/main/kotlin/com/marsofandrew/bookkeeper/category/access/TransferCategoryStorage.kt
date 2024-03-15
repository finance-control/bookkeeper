package com.marsofandrew.bookkeeper.category.access

import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.access.entity.toSpendingUserCategoryEntity
import com.marsofandrew.bookkeeper.category.access.repository.TransferUserCategoryRepository
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import org.springframework.stereotype.Service

@Service
internal class TransferCategoryStorage(
    private val transferUserCategoryRepository: TransferUserCategoryRepository
) : CategoryStorage<TransferUserCategory> {

    override fun findAllByUserId(userId: NumericId<User>): List<TransferUserCategory> {
        return transferUserCategoryRepository.findAllByUserId(userId.value).map { it.toModel() }
    }

    override fun existsByUserIdAndCategoryId(userId: NumericId<User>, id: NumericId<TransferUserCategory>): Boolean {
        return transferUserCategoryRepository.existsByIdAndUserId(id.value, userId.value)
    }

    override fun findAllByUserIdAndIds(
        userId: NumericId<User>,
        ids: Set<NumericId<TransferUserCategory>>
    ): List<TransferUserCategory> {
        return transferUserCategoryRepository.findAllByUserIdAndIdIn(userId.value, ids.mapTo(HashSet()) { it.value })
            .map { it.toModel() }
    }

    override fun delete(ids: Set<NumericId<TransferUserCategory>>) {
        transferUserCategoryRepository.deleteAllById(ids.map { it.value })
    }

    override fun create(userCategory: TransferUserCategory): TransferUserCategory {
        return transferUserCategoryRepository.saveAndFlush(userCategory.toSpendingUserCategoryEntity()).toModel()
    }
}
