package com.marsofandrew.bookkeeper.transfer.exception

import com.marsofandrew.bookkeeper.properties.exception.ValidationException
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.transfer.account.Account

class InvalidAccountException(accountId: StringId<Account>) :
    ValidationException("Account with id $accountId does not exists")