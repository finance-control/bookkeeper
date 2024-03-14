package com.marsofandrew.bookkeeper.user.controller.exception

import com.marsofandrew.bookkeeper.controller.dto.ErrorMessages

internal data class IncorrectEmailUserMessages(
    override val messages: List<String>,
    val email: String
) : ErrorMessages(messages)
