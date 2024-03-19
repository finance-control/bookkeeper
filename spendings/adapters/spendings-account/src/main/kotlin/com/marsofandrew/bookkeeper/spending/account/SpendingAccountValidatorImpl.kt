package com.marsofandrew.bookkeeper.spending.account

import com.marsofandrew.bookkeeper.account.AccountValidation
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.user.User
import org.springframework.stereotype.Service

@Service
internal class SpendingAccountValidatorImpl(
    private val accountValidation: AccountValidation
) : SpendingAccountValidator {

    override fun validate(userId: NumericId<User>, accountId: StringId<Account>): Boolean {
        return accountValidation.validate(userId.value.asId(), accountId.value.asId())
    }
}
