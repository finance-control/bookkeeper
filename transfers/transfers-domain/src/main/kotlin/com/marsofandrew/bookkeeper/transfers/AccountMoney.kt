package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.transfers.account.Account

data class AccountMoney(
    val money: PositiveMoney,
    val accountId: StringId<Account>?,
) {
    companion object {
        fun create(money: PositiveMoney) = AccountMoney(money, null)
    }
}
