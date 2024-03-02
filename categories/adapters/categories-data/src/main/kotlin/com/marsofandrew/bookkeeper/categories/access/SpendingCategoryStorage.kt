package com.marsofandrew.bookkeeper.categories.access

import com.marsofandrew.bookkeeper.categories.SpendingUserCategory
import com.marsofandrew.bookkeeper.categories.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import java.util.concurrent.atomic.AtomicLong
import org.springframework.stereotype.Service

@Service
internal class SpendingCategoryStorage : CategoryStorage<SpendingUserCategory> {

    private val counter = AtomicLong(1)

    private val categoriesByUser: MutableMap<NumericId<User>, MutableList<SpendingUserCategory>> = mutableMapOf()
    private val categoriesById: MutableMap<NumericId<SpendingUserCategory>, SpendingUserCategory> = mutableMapOf()
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

    override fun delete(ids: Set<NumericId<SpendingUserCategory>>) {
        categoriesById.filterValues { it.id in ids }
            .forEach { (_, userCategory) ->
                categoriesByUser[userCategory.userId]?.removeIf { it.id == userCategory.id }
                categoriesById.remove(userCategory.id)
            }
    }

    override fun create(userCategory: SpendingUserCategory): SpendingUserCategory {
        val categories = categoriesByUser.getOrDefault(userCategory.userId, mutableListOf())
        val forSave = userCategory.copy(id = counter.getAndIncrement().asId())
        categories.add(forSave)
        categoriesByUser[userCategory.userId] = categories
        categoriesById[forSave.id] = forSave
        return forSave
    }
}