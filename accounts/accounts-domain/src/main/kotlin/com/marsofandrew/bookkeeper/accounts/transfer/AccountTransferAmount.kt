package com.marsofandrew.bookkeeper.accounts.transfer

import com.marsofandrew.bookkeeper.accounts.Account
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.StringId

data class AccountTransferAmount(
    val accountId: StringId<Account>,
    val money: PositiveMoney
)
