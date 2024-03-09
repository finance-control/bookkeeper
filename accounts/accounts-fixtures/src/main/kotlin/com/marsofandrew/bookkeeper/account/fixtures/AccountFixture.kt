package com.marsofandrew.bookkeeper.account.fixtures

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import java.time.LocalDate

data class AccountFixture(
    val id: StringId<Account>,
    val userId: NumericId<User>,
) {
    var money: Money = Money(Currency.EUR, 0, 0)
    var title: String = "test account"
    var openedAt: LocalDate = LocalDate.now()
    var closedAt: LocalDate? = null
    var status: Account.Status = Account.Status.IN_USE
    var version: Version = Version(0)

    fun build() = Account(
        id = id,
        userId = userId,
        money = money,
        title = title,
        openedAt = openedAt,
        closedAt = closedAt,
        status = status,
        version = version
    )
}