package com.marsofandrew.bookkeeper.account

import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.exception.validateFiled
import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import java.time.LocalDate

data class Account(
    override val id: StringId<Account>,
    val userId: NumericId<User>,
    val money: Money,
    val title: String,
    val openedAt: LocalDate,
    val closedAt: LocalDate?,
    val status: Status,
    override val version: Version
) : DomainModel {

    init {
        validateFiled(title.isNotBlank()) { "title of Account $id is empty or blank" }
    }

    enum class Status {
        IN_USE,
        FOR_REMOVAL
    }

    fun topUp(money: PositiveMoney): Account {
        check(closedAt == null) { "Account $id is already closed" }
        check(status != Status.FOR_REMOVAL) { "Account $id is for removal" }
        return copy(money = this.money + money)
    }

    fun withdraw(money: PositiveMoney): Account {
        check(closedAt == null) { "Account $id is already closed" }
        check(status != Status.FOR_REMOVAL) { "Account $id is for removal" }
        return copy(money = this.money - money)
    }
}