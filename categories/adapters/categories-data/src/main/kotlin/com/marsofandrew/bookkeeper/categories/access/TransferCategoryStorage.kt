package com.marsofandrew.bookkeeper.categories.access

import com.marsofandrew.bookkeeper.categories.TransferUserCategory
import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import java.util.concurrent.atomic.AtomicLong
import org.springframework.stereotype.Service

@Service
internal class TransferCategoryStorage : CategoryStorage<TransferUserCategory> {

    private val counter = AtomicLong(0)

    private val categoriesByUser: MutableMap<NumericId<User>, MutableList<TransferUserCategory>> = mutableMapOf()
    override fun findAllByUserId(userId: NumericId<User>): List<TransferUserCategory> {
        return categoriesByUser.getOrDefault(userId, listOf())
    }

    override fun findAllByUserIdAndIds(
        userId: NumericId<User>,
        ids: Set<NumericId<TransferUserCategory>>
    ): List<TransferUserCategory> {
        return findAllByUserId(userId)
            .filter { it.id in ids }
    }

    override fun delete(userId: NumericId<User>, ids: Set<NumericId<TransferUserCategory>>) {
        val categories = categoriesByUser[userId] ?: return
        categories.removeIf { it.id in ids }
    }

    override fun create(userCategory: TransferUserCategory): TransferUserCategory {
        val categories = categoriesByUser.getOrDefault(userCategory.userId, mutableListOf())
        val forSave = userCategory.copy(id = counter.getAndIncrement().asId())
        categories.add(forSave)
        categoriesByUser[userCategory.userId] = categories

        return forSave
    }
}