package com.marsofandrew.bookkeeper.accounts.fixtures

import com.marsofandrew.bookkeeper.accounts.Account
import com.marsofandrew.bookkeeper.accounts.user.User
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

    fun build() = Account(
        id = id,
        userId = userId,
        money = money,
        title = title,
        openedAt = openedAt,
        closedAt = closedAt,
        status = status
    )
}