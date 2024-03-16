package com.marsofandrew.bookkeeper.account.access

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import java.time.LocalDate

interface AccountStorage {

    fun findAllByUserId(userId: NumericId<User>): Set<Account>
    fun findByUserIdAndIdOrThrow(userId: NumericId<User>, id: StringId<Account>): Account
    fun existsByUserIdAndAccountId(userId: NumericId<User>, id: StringId<Account>): Boolean
    fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Set<StringId<Account>>): Set<Account>
    fun findAccountsForRemoval(limit: Int): Set<Account>

    fun create(account: Account): Account
    fun setMoney(id: StringId<Account>, money: Money)
    fun setForRemovalAndClose(ids: Set<StringId<Account>>, closedAt: LocalDate)
    fun delete(ids: Set<StringId<Account>>)
}