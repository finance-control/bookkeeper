package com.marsofandrew.bookkeeper.categories.access

import com.marsofandrew.bookkeeper.categories.SpendingUserCategory
import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import java.util.concurrent.atomic.AtomicLong
import org.springframework.stereotype.Service

@Service
internal class SpendingCategoryStorage : CategoryStorage<SpendingUserCategory> {

    private val counter = AtomicLong(0)

    private val categoriesByUser: MutableMap<NumericId<User>, MutableList<SpendingUserCategory>> = mutableMapOf()
    override fun findAllByUserId(userId: NumericId<User>): List<SpendingUserCategory> {
        return categoriesByUser.getOrDefault(userId, listOf())
    }

    override fun findAllByUserIdAndIds(
        userId: NumericId<User>,
        ids: Set<NumericId<SpendingUserCategory>>
    ): List<SpendingUserCategory> {
        return findAllByUserId(userId)
            .filter { it.id in ids }
    }

    override fun delete(userId: NumericId<User>, ids: Set<NumericId<SpendingUserCategory>>) {
        val categories = categoriesByUser[userId] ?: return
        categories.removeIf { it.id in ids }
    }

    override fun create(userCategory: SpendingUserCategory): SpendingUserCategory {
        val categories = categoriesByUser.getOrDefault(userCategory.userId, mutableListOf())
        val forSave = userCategory.copy(id = counter.getAndIncrement().asId())
        categories.add(forSave)
        categoriesByUser[userCategory.userId] = categories

        return forSave
    }
}