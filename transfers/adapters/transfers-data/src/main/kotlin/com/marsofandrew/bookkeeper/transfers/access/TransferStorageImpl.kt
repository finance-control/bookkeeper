package com.marsofandrew.bookkeeper.transfers.access

import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import java.time.LocalDate
import java.util.UUID
import org.springframework.stereotype.Repository

@Repository
internal class TransferStorageImpl(
    private val transfersByUserId: MutableMap<NumericId<User>, MutableSet<Transfer>>,
    private val transferById: MutableMap<StringId<Transfer>, Transfer>
) : TransferStorage {

    override fun findAllByUserId(userId: NumericId<User>): List<Transfer> {
        return transfersByUserId.getOrDefault(userId, setOf()).toList()
    }

    override fun findAllByUserIdBetween(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Transfer> {
        return findAllByUserId(userId)
            .filter { it.date in startDate..endDate }
    }

    override fun create(transfer: Transfer): Transfer {
        // TODO: check that spending does not identified
        val id = UUID.randomUUID().toString().asId<Transfer>()

        val spendingForSave = transfer.copy(id = id)
        transferById[id] = spendingForSave
        transfersByUserId.putIfAbsent(transfer.userId, mutableSetOf())
        transfersByUserId.getValue(transfer.userId).add(spendingForSave)
        return spendingForSave
    }

    override fun delete(ids: Collection<StringId<Transfer>>) {
        ids.forEach { id ->
            val transfer = transferById[id] ?: return

            transferById.remove(id)
            transfersByUserId[transfer.userId]?.remove(transfer)
        }
    }
}