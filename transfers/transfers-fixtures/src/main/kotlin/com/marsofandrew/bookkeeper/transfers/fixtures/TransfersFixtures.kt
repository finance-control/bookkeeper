package com.marsofandrew.bookkeeper.transfers.fixtures

import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId

fun transfer(id: NumericId<Transfer>, userId: NumericId<User>, init: TransferFixture.() -> Unit = {}): Transfer =
    TransferFixture(id, userId).apply(init).build()