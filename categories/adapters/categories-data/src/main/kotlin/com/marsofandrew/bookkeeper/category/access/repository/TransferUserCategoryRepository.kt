package com.marsofandrew.bookkeeper.category.access.repository

import com.marsofandrew.bookkeeper.category.access.entity.TransferUserCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface TransferUserCategoryRepository : JpaRepository<TransferUserCategoryEntity, Long> {

    fun findAllByUserId(userId: Long): List<TransferUserCategoryEntity>

    fun findAllByUserIdAndIdIn(userId: Long, ids: Set<Long>): List<TransferUserCategoryEntity>

    fun existsByIdAndUserId(id: Long, userId: Long): Boolean
}