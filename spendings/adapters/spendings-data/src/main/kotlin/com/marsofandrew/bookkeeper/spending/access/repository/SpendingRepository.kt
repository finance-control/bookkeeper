package com.marsofandrew.bookkeeper.spending.access.repository

import com.marsofandrew.bookkeeper.spending.access.entity.SpendingEntity
import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface SpendingRepository : JpaRepository<SpendingEntity, Long> {

    fun findAllByUserIdAndIdIn(userId: Long, ids: Set<Long>): List<SpendingEntity>

    fun findAllByUserId(userId: Long): List<SpendingEntity>

    fun findAllByUserIdAndDateBetween(userId: Long, startDate: LocalDate, endDate: LocalDate): List<SpendingEntity>
}