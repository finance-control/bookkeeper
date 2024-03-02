package com.marsofandrew.bookkeeper.spendings.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicLong
import org.springframework.stereotype.Repository

@Repository
internal class SpendingStorageImpl(
    private val spendingsByUserId: MutableMap<NumericId<User>, MutableSet<Spending>>,
    private val spendingById: MutableMap<NumericId<Spending>, Spending>
) : SpendingStorage {

    private val counter = AtomicLong(1)

    override fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Collection<NumericId<Spending>>): Set<Spending> {
        return findAllByUserId(userId)
            .filter { it.id in ids }
            .toSet()
    }

    override fun findAllByUserId(userId: NumericId<User>): List<Spending> {
        return spendingsByUserId.getOrDefault(userId, setOf()).toList()
    }

    override fun findAllByUserIdBetween(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Spending> {
        return findAllByUserId(userId)
            .filter { it.date in startDate..endDate }
    }

    override fun create(spending: Spending): Spending {
        // TODO: check that spending does not identified
        val id = counter.getAndIncrement().asId<Spending>()

        val spendingForSave = spending.copy(id = id)
        spendingById[id] = spendingForSave
        spendingsByUserId.putIfAbsent(spending.userId, mutableSetOf())
        spendingsByUserId.getValue(spending.userId).add(spendingForSave)
        return spendingForSave
    }

    override fun delete(ids: Collection<NumericId<Spending>>) {
        ids.forEach { id ->
            val spending = spendingById[id] ?: return

            spendingById.remove(id)
            spendingsByUserId[spending.userId]?.remove(spending)
        }
    }
}