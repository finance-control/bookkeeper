package com.marsofandrew.bookkeeper.transfer.access.repository

import com.marsofandrew.bookkeeper.transfer.access.entity.TransferEntity
import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface TransferRepository : JpaRepository<TransferEntity, Long> {

    fun findAllByUserIdAndIdInOrderByDate(userId: Long, ids: Set<Long>): List<TransferEntity>

    fun findAllByUserIdOrderByDate(userId: Long): List<TransferEntity>

    fun findAllByUserIdAndDateBetweenOrderByDate(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<TransferEntity>
}