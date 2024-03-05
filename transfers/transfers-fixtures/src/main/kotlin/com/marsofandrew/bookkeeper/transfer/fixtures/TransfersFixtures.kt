package com.marsofandrew.bookkeeper.transfer.fixtures

import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

fun transfer(id: NumericId<Transfer>, userId: NumericId<User>, init: TransferFixture.() -> Unit = {}): Transfer =
    TransferFixture(id, userId).apply(init).build()