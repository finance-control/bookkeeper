package com.marsofandrew.bookkeeper.account.transfer

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.StringId

data class AccountTransferAmount(
    val accountId: StringId<Account>,
    val money: PositiveMoney
)
