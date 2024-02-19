package com.marsofandrew.bookkeeper.spendings.impl

import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.access.SpendingStorage
import com.marsofandrew.bookkeeper.spendings.SpendingAdding

class SpendingAddingImpl(
    private val spendingStorage: SpendingStorage
) : SpendingAdding {

    override fun add(spending: Spending): Spending {
        return spendingStorage.create(spending)
    }
}