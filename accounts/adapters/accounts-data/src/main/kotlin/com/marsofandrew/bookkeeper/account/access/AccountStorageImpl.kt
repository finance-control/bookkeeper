package com.marsofandrew.bookkeeper.account.access

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.exception.orElseThrow
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import java.time.Clock
import java.time.LocalDate
import java.util.Objects
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Service

@Service
internal class AccountStorageImpl(
    private val clock: Clock
) : AccountStorage {

    private val accountById: ConcurrentHashMap<StringId<Account>, Account> = ConcurrentHashMap()
    private val accountsByUserId: ConcurrentHashMap<NumericId<User>, MutableList<Account>> = ConcurrentHashMap()
    override fun findAllByUserId(userId: NumericId<User>): Set<Account> {
        return accountsByUserId.getOrDefault(userId, listOf()).toSet()
    }

    override fun findByUserIdAndIdOrThrow(userId: NumericId<User>, id: StringId<Account>): Account {
        return accountsByUserId[userId]?.singleOrNull { it.id == id }.orElseThrow(id)
    }

    override fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Set<StringId<Account>>): Set<Account> {
        return findAllByUserId(userId).filter {
            it.id in ids
        }.toSet()
    }

    override fun findAccountsForRemoval(limit: Int): Set<Account> {
        return accountById.values.filter {
            it.status == Account.Status.FOR_REMOVAL
        }.toSet()
    }

    override fun create(account: Account): Account {
        check(!account.id.initialized) { "Attempt to create identified account" }
        val accountForSaving = account.identify()

        accountById[accountForSaving.id] = accountForSaving
        val usersAccounts = accountsByUserId.getOrDefault(account.userId, mutableListOf())
        usersAccounts.add(accountForSaving)
        accountsByUserId[accountForSaving.userId] = usersAccounts

        return accountForSaving
    }

    override fun setMoney(id: StringId<Account>, money: Money) {
        val account = accountById[id]?.copy(money = money) ?: return

        accountById[id] = account
        requireNotNull(accountsByUserId[account.userId]).removeIf { it.id == id }
        requireNotNull(accountsByUserId[account.userId]).add(account)
    }

    override fun setForRemovalAndClose(ids: Set<StringId<Account>>, closedAt: LocalDate) {
        accountById.filterKeys { it in ids }
            .values
            .forEach { account ->
                val closedAccount = account.copy(status = Account.Status.FOR_REMOVAL, closedAt = closedAt)
                accountById[account.id] = closedAccount
                accountsByUserId[account.userId]?.remove(account)
                accountsByUserId[account.userId]?.add(closedAccount)
            }
    }

    override fun delete(ids: Set<StringId<Account>>) {
        accountById.filterKeys { it in ids }
            .values
            .forEach { account ->
                accountsByUserId[account.userId]?.removeIf { it.id == account.id }
                accountById.remove(account.id)
            }
    }

    private fun Account.identify() =
        copy(id = "${userId.value}-${Objects.hash(clock.instant(), title, openedAt)}".asId())
}