package com.marsofandrew.bookkeeper.account.access.repository

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.access.entity.AccountEntity
import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface AccountRepository : JpaRepository<AccountEntity, String> {

    fun findAllByUserId(userId: Long): List<AccountEntity>

    fun findByUserIdAndId(userId: Long, id: String): AccountEntity?

    fun findAllByUserIdAndIdIn(userId: Long, ids: Collection<String>): List<AccountEntity>

    fun findAllByStatusEquals(status: Account.Status): List<AccountEntity>
    fun existsByUserIdAndId(userId: Long, id: String): Boolean

    @Modifying
    @Query(
        """
            UPDATE bookkeeper.account
            SET status = 'FOR_REMOVAL', closed_at = :closedAt
            WHERE id IN :ids
        """,
        nativeQuery = true
    )
    fun setForRemovalAndClose(ids: Collection<String>, closedAt: LocalDate)
}