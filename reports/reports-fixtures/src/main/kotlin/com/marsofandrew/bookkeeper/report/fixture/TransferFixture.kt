package com.marsofandrew.bookkeeper.report.fixture

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.transfer.Transfer
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class TransferFixture(
    val userId: NumericId<User>,
) {
    var date: LocalDate = LocalDate.now()
    var send: PositiveMoney = PositiveMoney(Currency.EUR, 100)
    var received: PositiveMoney = PositiveMoney(Currency.EUR, 100)
    var transferCategoryId: NumericId<TransferCategory> = 1.asId()

    fun build() = Transfer(
        userId = userId,
        date = date,
        send = send,
        received = received,
        transferCategoryId = transferCategoryId
    )
}
