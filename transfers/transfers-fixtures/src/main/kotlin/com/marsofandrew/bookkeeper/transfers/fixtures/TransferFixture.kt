package com.marsofandrew.bookkeeper.transfers.fixtures

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.category.TransferCategory
import com.marsofandrew.bookkeeper.transfers.user.User
import java.math.BigDecimal
import java.time.LocalDate

class TransferFixture(
    val id: NumericId<Transfer>,
    val userId: NumericId<User>,
) {
    var date: LocalDate = LocalDate.now()
    var send: AccountMoney? = null
    var received: AccountMoney = AccountMoney.create(PositiveMoney(Currency.EUR, BigDecimal.ONE))
    var comment: String = ""
    var transferCategoryId: NumericId<TransferCategory> = 1.asId()
    var createdAt: LocalDate = LocalDate.now()

    fun build() = Transfer(
        id,
        userId,
        date,
        send,
        received,
        comment,
        transferCategoryId,
        createdAt,
    )
}