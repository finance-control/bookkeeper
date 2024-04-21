package com.marsofandrew.bookkeeper.transfers.fixtures

import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.Transfer

fun commonTransfer(
    id: NumericId<CommonTransfer>,
    userId: NumericId<User>,
    init: CommonTransferFixture.() -> Unit = {}
): CommonTransfer =
    CommonTransferFixture(id, userId).apply(init).build()

fun transfer(
    id: NumericId<CommonTransfer>,
    userId: NumericId<User>,
    init: TransferFixture.() -> Unit = {}
): Transfer = TransferFixture(id, userId).apply(init).build()

fun earning(
    id: NumericId<CommonTransfer>,
    userId: NumericId<User>,
    init: EarningFixture.() -> Unit = {}
): Earning = EarningFixture(id, userId).apply(init).build()
