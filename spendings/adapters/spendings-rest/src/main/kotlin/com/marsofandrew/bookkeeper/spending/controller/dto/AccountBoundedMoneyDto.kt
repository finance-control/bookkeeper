package com.marsofandrew.bookkeeper.spending.controller.dto

internal data class AccountBoundedMoneyDto(
    val money: PositiveMoneyDto,
    val accountId: String?
)
