package com.marsofandrew.bookkeeper.transfer.account

import com.marsofandrew.bookkeeper.account.AccountValidation
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.user.User
import org.springframework.stereotype.Service

@Service
internal class TransferAccountValidatorImpl(
    private val accountValidation: AccountValidation
) : TransferAccountValidator {

    override fun validate(userId: NumericId<User>, accountId: StringId<Account>): Boolean {
        return accountValidation.validate(userId.value.asId(), accountId.value.asId())
    }
}
