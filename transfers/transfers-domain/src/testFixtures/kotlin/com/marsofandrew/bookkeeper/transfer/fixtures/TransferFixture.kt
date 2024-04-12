package com.marsofandrew.bookkeeper.transfer.fixtures

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.AccountMoney
import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.category.Category
import com.marsofandrew.bookkeeper.transfer.user.User
import java.math.BigDecimal
import java.time.LocalDate

class TransferFixture(
    val id: NumericId<Transfer>,
    val userId: NumericId<User>,
) {
    var date: LocalDate = LocalDate.now()
    var send: AccountMoney? = null
    var received: AccountMoney = AccountMoney.create(PositiveMoney(Currency.EUR, BigDecimal.ONE))
    var description: String = ""
    var categoryId: NumericId<Category> = 1.asId()
    var createdAt: LocalDate = LocalDate.now()
    var version = Version(0)

    fun build() = Transfer(
        id,
        userId,
        date,
        send,
        received,
        description,
        categoryId,
        createdAt,
        version
    )
}