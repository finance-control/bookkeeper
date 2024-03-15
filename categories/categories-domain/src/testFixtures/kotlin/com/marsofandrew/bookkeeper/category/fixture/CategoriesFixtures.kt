package com.marsofandrew.bookkeeper.category.fixture

import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.properties.id.NumericId

fun spendingUserCategory(
    id: NumericId<SpendingUserCategory>,
    init: SpendingUserCategoryFixture.() -> Unit = {}
): SpendingUserCategory = SpendingUserCategoryFixture(id).apply(init).build()

fun transferUserCategory(
    id: NumericId<TransferUserCategory>,
    init: TransferUserCategoryFixture.() -> Unit = {}
): TransferUserCategory = TransferUserCategoryFixture(id).apply(init).build()
