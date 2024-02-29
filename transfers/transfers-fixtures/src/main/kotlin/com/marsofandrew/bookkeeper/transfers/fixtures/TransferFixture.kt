package com.marsofandrew.bookkeeper.transfers.fixtures

import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.category.TransferCategory
import com.marsofandrew.bookkeeper.transfers.user.User
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import java.math.BigDecimal
import java.time.LocalDate

class TransferFixture(
    val id: StringId<Transfer>,
    val userId: NumericId<User>,
) {
    var date: LocalDate = LocalDate.now()
    var send: PositiveMoney? = null
    var fee: PositiveMoney? = null
    var received: PositiveMoney = PositiveMoney(Currency.EUR, BigDecimal.ONE)
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
        fee
    )
}