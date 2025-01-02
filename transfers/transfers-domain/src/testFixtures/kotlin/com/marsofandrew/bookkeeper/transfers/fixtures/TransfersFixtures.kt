package com.marsofandrew.bookkeeper.transfers.fixtures

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.EarningUpdate
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.TransferUpdate
import com.marsofandrew.bookkeeper.transfers.category.Category
import com.marsofandrew.bookkeeper.transfers.user.User

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

fun earningUpdate(
    id: NumericId<CommonTransfer>,
    init: EarningUpdateFixture.() -> Unit = {}
): EarningUpdate = EarningUpdateFixture(id).apply(init).build()

fun transferUpdate(
    id: NumericId<CommonTransfer>,
    init: TransferUpdateFixture.() -> Unit = {}
): TransferUpdate = TransferUpdateFixture(id).apply(init).build()

fun category(
    id: NumericId<Category>,
    init: CategoryFixture.() -> Unit = {}
): Category = CategoryFixture(id)
    .apply(init)
    .build()