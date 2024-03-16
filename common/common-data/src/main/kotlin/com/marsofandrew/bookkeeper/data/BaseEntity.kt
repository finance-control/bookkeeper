package com.marsofandrew.bookkeeper.data

import com.marsofandrew.bookkeeper.base.model.DomainModel

interface BaseEntity<ModelType : DomainModel> {

    fun toModel(): ModelType
}

fun <ModelType : DomainModel> List<BaseEntity<ModelType>>.toModels(): List<ModelType> =
    map { it.toModel() }

fun <ModelType : DomainModel> List<BaseEntity<ModelType>>.toModelsSet(): Set<ModelType> =
    mapTo(HashSet()) { it.toModel() }
