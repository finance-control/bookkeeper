package com.marsofandrew.bookkeeper.category.access.repository

import com.marsofandrew.bookkeeper.category.access.entity.SpendingUserCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface SpendingUserCategoryRepository : JpaRepository<SpendingUserCategoryEntity, Long> {

    fun findAllByUserId(userId: Long): List<SpendingUserCategoryEntity>

    fun findAllByUserIdAndIdIn(userId: Long, ids: Set<Long>): List<SpendingUserCategoryEntity>

    fun existsByIdAndUserId(id: Long, userId: Long): Boolean
}