package com.marsofandrew.bookkeeper.transfer.access

import com.marsofandrew.bookkeeper.data.toModels
import com.marsofandrew.bookkeeper.data.toModelsSet
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.access.entity.toTransferEntity
import com.marsofandrew.bookkeeper.transfer.access.repository.TransferRepository
import com.marsofandrew.bookkeeper.transfer.user.User
import java.time.LocalDate
import org.springframework.stereotype.Repository

@Repository
internal class TransferStorageImpl(
    private val transferRepository: TransferRepository
) : TransferStorage {

    override fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Collection<NumericId<Transfer>>): Set<Transfer> {
        return transferRepository.findAllByUserIdAndIdInOrderByDate(userId.value, ids.mapTo(HashSet()) { it.value })
            .toModelsSet()
    }

    override fun findAllByUserId(userId: NumericId<User>): List<Transfer> {
        return transferRepository.findAllByUserIdOrderByDate(userId.value).toModels()
    }

    override fun findAllByUserIdBetween(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Transfer> {
        return transferRepository.findAllByUserIdAndDateBetweenOrderByDate(userId.value, startDate, endDate).toModels()
    }

    override fun create(transfer: Transfer): Transfer {
        require(!transfer.id.initialized)

        return transferRepository.saveAndFlush(transfer.toTransferEntity()).toModel()
    }

    override fun delete(ids: Collection<NumericId<Transfer>>) {
        transferRepository.deleteAllById(ids.map { it.value })
    }
}