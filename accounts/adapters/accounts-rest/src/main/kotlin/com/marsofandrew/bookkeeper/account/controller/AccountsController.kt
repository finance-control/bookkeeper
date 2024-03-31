package com.marsofandrew.bookkeeper.account.controller

import com.marsofandrew.bookkeeper.account.AccountCreation
import com.marsofandrew.bookkeeper.account.AccountDeletion
import com.marsofandrew.bookkeeper.account.AccountSelection
import com.marsofandrew.bookkeeper.account.controller.dto.AccountDto
import com.marsofandrew.bookkeeper.account.controller.dto.CreateAccountDto
import com.marsofandrew.bookkeeper.account.controller.dto.toAccountDto
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import java.time.Clock
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/accounts")
internal class AccountsController(
    private val accountSelection: AccountSelection,
    private val accountDeletion: AccountDeletion,
    private val accountCreation: AccountCreation,
    private val clock: Clock
) {

    @GetMapping
    fun get(
        @Parameter(hidden = true) @UserId userId: Long
    ): List<AccountDto> {
        return accountSelection.select(userId.asId()).map { it.toAccountDto() }
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun create(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestBody body: CreateAccountDto
    ): AccountDto {
        return accountCreation.create(body.toAccount(userId.asId(), clock)).toAccountDto()
    }

    @PostMapping("/{id}/close")
    fun close(
        @Parameter(hidden = true) @UserId userId: Long,
        @PathVariable("id") id: String
    ) {
        accountDeletion.delete(userId.asId(), setOf(id.asId()))
    }
}
