package com.marsofandrew.bookkeeper.base.exception

import com.marsofandrew.bookkeeper.properties.exception.ValidationException
import com.marsofandrew.bookkeeper.properties.id.AbstractId
import com.marsofandrew.bookkeeper.properties.id.Id

class DomainModelNotFoundException : ValidationException {
    constructor(id: AbstractId<*>) : this("id", id.toString())
    constructor(
        parameterName: String,
        parameterValue: String
    ) : super("Domain model with $parameterName $parameterValue is not found")

    constructor(
        id: AbstractId<*>,
        ownerId: AbstractId<*>
    ) : super("Domain model with id $id is not found for owner $ownerId")
}

fun <T> T?.orElseThrow(id: AbstractId<*>): T = this ?: throw DomainModelNotFoundException(id)

fun <T> T?.orElseThrow(id: AbstractId<*>, ownerId: AbstractId<*>): T =
    this ?: throw DomainModelNotFoundException(id, ownerId)

fun <T> T?.orElseThrow(parameterName: String, parameterValue: String): T =
    this ?: throw DomainModelNotFoundException(parameterName, "{$parameterValue}")
