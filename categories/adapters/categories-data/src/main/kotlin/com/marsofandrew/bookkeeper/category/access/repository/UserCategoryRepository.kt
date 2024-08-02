package com.marsofandrew.bookkeeper.category.access.repository

import com.marsofandrew.bookkeeper.category.access.entity.UserCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface UserCategoryRepository : JpaRepository<UserCategoryEntity, Long> {

    fun findAllByUserId(userId: Long): List<UserCategoryEntity>

    fun findAllByUserIdAndIdIn(userId: Long, ids: Set<Long>): List<UserCategoryEntity>

    fun existsByIdAndUserId(id: Long, userId: Long): Boolean

    fun existsByUserIdAndTitle(userId: Long, title: String): Boolean
}