package com.marsofandrew.bookkeeper.base.exception

import com.marsofandrew.bookkeeper.properties.id.AbstractId

class DomainModelNotFoundException(id: AbstractId<*>) : RuntimeException("Domain model with id $id is not found")

fun <T> T?.orElseThrow(id: AbstractId<*>): T = this ?: throw DomainModelNotFoundException(id)