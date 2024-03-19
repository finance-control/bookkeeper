package com.marsofandrew.bookkeeper.base.model

import com.marsofandrew.bookkeeper.properties.id.Id

interface DomainModel {
    val id: Id<*>
    val version: Version
}
