package com.marsofandrew.bookkeeper.base.exception

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.properties.id.Id

class DomainModelNotFoundException(id: Id<*>) : RuntimeException("Domain model with id $id is not found")

fun <T : DomainModel> T?.orElseThrow(id: Id<*>): T = this ?: throw DomainModelNotFoundException(id)