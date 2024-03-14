package com.marsofandrew.bookkeeper.credentials.repository

import com.marsofandrew.bookkeeper.credentials.entity.CredentialsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface CredentialsRepository : JpaRepository<CredentialsEntity, Long> {

    fun existsByEmailAndUserIdNot(email: String, userId: Long): Boolean
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): CredentialsEntity?
}
