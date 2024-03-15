package com.marsofandrew.bookkeeper.data

import com.marsofandrew.bookkeeper.base.model.DomainModel

interface BaseEntity<ModelType : DomainModel> {

    fun toModel(): ModelType
}