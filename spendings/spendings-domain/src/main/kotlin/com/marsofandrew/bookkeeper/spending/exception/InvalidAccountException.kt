package com.marsofandrew.bookkeeper.spending.exception

import com.marsofandrew.bookkeeper.base.exception.ValidationException
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spending.account.Account

class InvalidAccountException(accountId: StringId<Account>) :
    ValidationException("Account with id $accountId does not exists")