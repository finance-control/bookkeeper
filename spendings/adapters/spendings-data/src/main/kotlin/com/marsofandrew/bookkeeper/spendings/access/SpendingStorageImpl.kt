package com.marsofandrew.bookkeeper.spendings.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.LocalDate
import java.util.UUID
import org.springframework.stereotype.Repository

@Repository
internal class SpendingStorageImpl(
    private val spendingsByUserId: MutableMap<NumericId<User>, MutableSet<Spending>>,
    private val spendingById: MutableMap<StringId<Spending>, Spending>
) : SpendingStorage {

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
        val id = UUID.randomUUID().toString().asId<Spending>()

        val spendingForSave = spending.copy(id = id)
        spendingById[id] = spendingForSave
        spendingsByUserId.putIfAbsent(spending.userId, mutableSetOf())
        spendingsByUserId.getValue(spending.userId).add(spendingForSave)
        return spendingForSave
    }

    override fun delete(ids: Collection<StringId<Spending>>) {
        ids.forEach { id ->
            val spending = spendingById[id] ?: return

            spendingById.remove(id)
            spendingsByUserId[spending.userId]?.remove(spending)
        }
    }
}