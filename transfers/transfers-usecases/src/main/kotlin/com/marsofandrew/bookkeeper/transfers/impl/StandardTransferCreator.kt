package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.transfers.exception.InvalidCategoryException
import com.marsofandrew.bookkeeper.transfers.impl.utils.toAccountBoundedMoney
import com.marsofandrew.bookkeeper.transfers.user.User
import org.apache.logging.log4j.LogManager

internal class StandardTransferCreator<T: CommonTransferBase>(
    private val transferStorage: TransferStorage,
    private val eventPublisher: EventPublisher,
    private val transferAccountValidator: TransferAccountValidator,
    private val transferCategoryValidator: TransferCategoryValidator,
    private val mapper: (CommonTransferBase) -> T
) {

    private val logger = LogManager.getLogger()

    fun add(commonTransfer: T): T {
        if (!transferCategoryValidator.validate(commonTransfer.userId, commonTransfer.categoryId)) {
            throw InvalidCategoryException(commonTransfer.categoryId)
        }
        transferAccountValidator.validate(commonTransfer.userId, commonTransfer.received)
        commonTransfer.send?.let { transferAccountValidator.validate(commonTransfer.userId, it) }

        val createdTransfer = transferStorage.create(commonTransfer)
        logger.info("CommonTransfer with id ${createdTransfer.id} was created")

        eventPublisher.publish(createdTransfer.toMoneyIsTransferredEvent())
        return mapper(createdTransfer)
    }
}

private fun CommonTransferBase.toMoneyIsTransferredEvent() = MoneyIsTransferredEvent(
    userId = userId.value,
    date = date,
    send = send?.toAccountBoundedMoney(),
    received = received.toAccountBoundedMoney(),
    category = categoryId.value
)

private fun TransferAccountValidator.validate(userId: NumericId<User>, accountMoney: AccountMoney) {
    if (accountMoney.accountId?.let { validate(userId, it) } == false) {
        throw InvalidAccountException(accountMoney.accountId!!)
    }
}
