package com.marsofandrew.bookkeeper.spendings.controller.dto

internal data class AccountBoundedMoney(
    val money: PositiveMoneyDto,
    val accountId: String?
)
