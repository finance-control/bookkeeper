package com.marsofandrew.bookkeeper.spendings

import com.marsofandrew.bookkeeper.properties.id.StringId

interface SpendingDeletion {

    fun delete(ids: Collection<StringId<Spending>>) //TODO: check by userId
}