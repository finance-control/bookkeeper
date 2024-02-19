package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.properties.id.StringId

interface TransferDeletion {

    fun delete(ids: Collection<StringId<Transfer>>)
}