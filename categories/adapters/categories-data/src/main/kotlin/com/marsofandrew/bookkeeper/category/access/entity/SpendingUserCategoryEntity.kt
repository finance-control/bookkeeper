package com.marsofandrew.bookkeeper.category.access.entity

import com.marsofandrew.bookkeeper.category.SpendingUserCategory
import com.marsofandrew.bookkeeper.data.BaseEntity
import com.marsofandrew.bookkeeper.properties.id.asId
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version

@Entity
@Table(name = "spending_category", schema = "bookkeeper")
internal data class SpendingUserCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookkeeper.spending_category_id_seq")
    var id: Long?,
    var userId: Long,
    var title: String,
    var description: String?,
    @Version
    var version: Int
) : BaseEntity<SpendingUserCategory> {

    override fun toModel(): SpendingUserCategory = SpendingUserCategory(
        id = requireNotNull(id).asId(),
        userId = userId.asId(),
        title = title,
        description = description,
        version = com.marsofandrew.bookkeeper.base.model.Version(version)
    )
}

internal fun SpendingUserCategory.toSpendingUserCategoryEntity() = SpendingUserCategoryEntity(
    id = id.rawValue,
    userId = userId.value,
    title = title,
    description = description,
    version = version.value
)
