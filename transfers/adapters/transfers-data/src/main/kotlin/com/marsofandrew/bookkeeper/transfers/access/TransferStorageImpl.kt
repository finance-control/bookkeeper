package com.marsofandrew.bookkeeper.transfers.access

import com.marsofandrew.bookkeeper.data.toModels
import com.marsofandrew.bookkeeper.data.toModelsSet
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.access.entity.toTransferEntity
import com.marsofandrew.bookkeeper.transfers.access.repository.TransferRepository
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate
import org.springframework.stereotype.Repository

@Repository
internal class TransferStorageImpl(
    private val transferRepository: TransferRepository
) : TransferStorage {

    override fun findAllByUserIdAndIds(
        userId: NumericId<User>,
        ids: Collection<NumericId<CommonTransfer>>
    ): Set<CommonTransferBase> {
        return transferRepository.findAllByUserIdAndIdInOrderByDate(userId.value, ids.mapTo(HashSet()) { it.value })
            .toModelsSet()
    }

    override fun findAllByUserId(userId: NumericId<User>): List<CommonTransfer> {
        return transferRepository.findAllByUserIdOrderByDate(userId.value).toModels()
    }

    override fun findAllByUserIdBetween(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<CommonTransferBase> {
        return transferRepository.findAllByUserIdAndDateBetweenOrderByDate(userId.value, startDate, endDate).toModels()
    }

    override fun create(commonTransfer: CommonTransferBase): CommonTransfer {
        require(!commonTransfer.id.initialized)

        return transferRepository.saveAndFlush(commonTransfer.toTransferEntity()).toModel()
    }

    override fun delete(ids: Collection<NumericId<CommonTransfer>>) {
        transferRepository.deleteAllById(ids.map { it.value })
    }
}