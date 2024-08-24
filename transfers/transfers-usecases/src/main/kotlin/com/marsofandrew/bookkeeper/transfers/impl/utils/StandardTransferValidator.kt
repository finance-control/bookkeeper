package com.marsofandrew.bookkeeper.transfers.impl.utils

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.transfers.exception.InvalidCategoryException
import com.marsofandrew.bookkeeper.transfers.user.User

internal class StandardTransferValidator(
    private val transferAccountValidator: TransferAccountValidator,
    private val transferCategoryValidator: TransferCategoryValidator,
) {

    fun validate(commonTransfer: CommonTransferBase) {
        if (!transferCategoryValidator.validate(commonTransfer.userId, commonTransfer.categoryId)) {
            throw InvalidCategoryException(commonTransfer.categoryId)
        }
        transferAccountValidator.validate(commonTransfer.userId, commonTransfer.received)
        commonTransfer.send?.let { transferAccountValidator.validate(commonTransfer.userId, it) }
    }
}

private fun TransferAccountValidator.validate(userId: NumericId<User>, accountMoney: AccountMoney) {
    if (accountMoney.accountId?.let { validate(userId, it) } == false) {
        throw InvalidAccountException(accountMoney.accountId!!)
    }
}
