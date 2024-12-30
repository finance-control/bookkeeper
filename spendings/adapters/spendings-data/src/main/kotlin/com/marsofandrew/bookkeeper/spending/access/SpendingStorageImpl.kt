package com.marsofandrew.bookkeeper.spending.access

import com.marsofandrew.bookkeeper.data.toModels
import com.marsofandrew.bookkeeper.data.toModelsSet
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.access.entity.toSpendingEntity
import com.marsofandrew.bookkeeper.spending.access.repository.SpendingRepository
import com.marsofandrew.bookkeeper.spending.user.User
import java.time.LocalDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
internal class SpendingStorageImpl(
    private val spendingRepository: SpendingRepository
) : SpendingStorage {

    override fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Collection<NumericId<Spending>>): Set<Spending> {
        return spendingRepository.findAllByUserIdAndIdInOrderByDate(userId.value, ids.mapTo(HashSet()) { it.value })
            .toModelsSet()
    }

    override fun findByIdAndUserId(id: NumericId<Spending>, userId: NumericId<User>): Spending? {
        return spendingRepository.findByIdAndUserId(id.value, userId.value)?.toModel()
    }

    override fun findAllByUserId(userId: NumericId<User>): List<Spending> {
        return spendingRepository.findAllByUserIdOrderByDate(userId.value).toModels()
    }

    override fun findAllByUserIdBetween(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Spending> {
        return spendingRepository.findAllByUserIdAndDateBetweenOrderByDate(userId.value, startDate, endDate).toModels()
    }

    @Transactional
    override fun create(spending: Spending): Spending {
        return spendingRepository.saveAndFlush(spending.toSpendingEntity()).toModel()
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun update(spending: Spending): Spending {
       return spendingRepository.saveAndFlush(spending.toSpendingEntity()).toModel()
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun delete(ids: Collection<NumericId<Spending>>) {
        spendingRepository.deleteAllById(ids.map { it.value })
    }
}
