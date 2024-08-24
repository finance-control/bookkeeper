package com.marsofandrew.bookkeeper.transfers.impl.earning

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.EarningUpdate
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.earning.EarningModification
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransferModifier
import com.marsofandrew.bookkeeper.transfers.updateEarning
import com.marsofandrew.bookkeeper.transfers.user.User

class EarningModificationImpl(
    transferStorage: TransferStorage,
    eventPublisher: EventPublisher,
    transferAccountValidator: TransferAccountValidator,
    transferCategoryValidator: TransferCategoryValidator,
    transactionExecutor: TransactionExecutor
) : EarningModification {

    private val modifier = StandardTransferModifier(
        transferStorage,
        eventPublisher,
        transferAccountValidator,
        transferCategoryValidator,
        transactionExecutor,
        Earning::of,
        CommonTransferBase::updateEarning
    )

    override fun modify(userId: NumericId<User>, earning: EarningUpdate): Earning {
        return modifier.modify(userId, earning)
    }
}