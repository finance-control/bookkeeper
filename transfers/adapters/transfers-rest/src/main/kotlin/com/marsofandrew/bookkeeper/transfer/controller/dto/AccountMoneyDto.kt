package com.marsofandrew.bookkeeper.transfer.controller.dto

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.AccountMoney

internal data class AccountMoneyDto(
    val money: PositiveMoneyDto,
    val accountId: String? = null,
)

internal fun AccountMoneyDto.toAccountMoney() = AccountMoney(
    accountId = accountId?.asId(),
    money = money.toPositiveMoney()
)

internal fun AccountMoney.toAccountsMoneyDto() = AccountMoneyDto(
    accountId = accountId?.value,
    money = money.toPositiveMoneyDto()
)