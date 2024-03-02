package com.marsofandrew.bookkeeper.spendings.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.LocalDate

interface SpendingStorage {

    fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Collection<NumericId<Spending>>): Set<Spending>
    fun findAllByUserId(userId: NumericId<User>): List<Spending>
    fun findAllByUserIdBetween(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): List<Spending>

    fun create(spending: Spending): Spending
    fun delete(ids: Collection<NumericId<Spending>>)
}