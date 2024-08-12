package com.marsofandrew.bookkeeper.spending.impl.util

import com.marsofandrew.bookkeeper.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.spending.Spending

fun Spending.toRollbackMoneyIsSendEvent() = RollbackMoneyIsSpendEvent(
    userId = userId.value,
    date = date,
    money = AccountBondedMoney(money, sourceAccountId?.value),
    category = categoryId.value
)

fun Spending.toMoneyIsSpendEvent() = MoneyIsSpendEvent(
    userId = userId.value,
    date = date,
    money = AccountBondedMoney(money, sourceAccountId?.value),
    category = categoryId.value
)