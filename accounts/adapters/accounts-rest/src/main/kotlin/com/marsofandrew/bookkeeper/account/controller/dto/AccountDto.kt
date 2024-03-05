package com.marsofandrew.bookkeeper.account.controller.dto

import com.marsofandrew.bookkeeper.account.Account

internal data class AccountDto(
    val id: String,
    val userId: Long,
    val money: MoneyDto,
    val title: String,
    val openedAt: String,
    val closedAt: String?,
    val status: Account.Status
)

internal fun Account.toAccountDto() = AccountDto(
    id = id.value,
    userId = userId.value,
    money = money.toMoneyDto(),
    title = title,
    openedAt = openedAt.toString(),
    closedAt = closedAt?.toString(),
    status = status
)