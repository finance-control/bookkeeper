package com.marsofandrew.bookkeeper.spendings.impl

import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.access.SpendingStorage
import com.marsofandrew.bookkeeper.spendings.SpendingDeletion

class SpendingDeletionImpl(
    private val spendingStorage: SpendingStorage
) : SpendingDeletion {

    override fun delete(ids: Collection<StringId<Spending>>) {
        spendingStorage.delete(ids)
    }
}