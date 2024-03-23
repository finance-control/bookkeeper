package com.marsofandrew.bookkeeper.transfer.impl

import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfer.AccountMoney
import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.TransferAdding
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfer.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfer.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.transfer.exception.InvalidCategoryException
import com.marsofandrew.bookkeeper.transfer.impl.utils.toAccountBoundedMoney
import com.marsofandrew.bookkeeper.transfer.user.User
import org.apache.logging.log4j.LogManager

class TransferAddingImpl(
    private val transferStorage: TransferStorage,
    private val eventPublisher: EventPublisher,
    private val transferAccountValidator: TransferAccountValidator,
    private val transferCategoryValidator: TransferCategoryValidator
) : TransferAdding {

    private val logger = LogManager.getLogger()

    override fun add(transfer: Transfer): Transfer {
        if (!transferCategoryValidator.validate(transfer.userId, transfer.transferCategoryId)) {
            throw InvalidCategoryException(transfer.transferCategoryId)
        }
        transferAccountValidator.validate(transfer.userId, transfer.received)
        transfer.send?.let { transferAccountValidator.validate(transfer.userId, it) }

        val createdTransfer = transferStorage.create(transfer)
        logger.info("Transfer with id ${createdTransfer.id} was created")

        eventPublisher.publish(createdTransfer.toMoneyIsTransferredEvent())
        return createdTransfer
    }
}

private fun Transfer.toMoneyIsTransferredEvent() = MoneyIsTransferredEvent(
    userId = userId.value,
    date = date,
    send = send?.toAccountBoundedMoney(),
    received = received.toAccountBoundedMoney(),
    category = transferCategoryId.value
)

private fun TransferAccountValidator.validate(userId: NumericId<User>, accountMoney: AccountMoney) {
    if (accountMoney.accountId?.let { validate(userId, it) } == false) {
        throw InvalidAccountException(accountMoney.accountId!!)
    }
}
