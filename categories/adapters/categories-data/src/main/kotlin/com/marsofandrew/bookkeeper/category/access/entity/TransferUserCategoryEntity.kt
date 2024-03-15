package com.marsofandrew.bookkeeper.category.access.entity

import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.data.BaseEntity
import com.marsofandrew.bookkeeper.properties.id.asId
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version

@Entity
@Table(name = "transfer_category", schema = "bookkeeper")
internal data class TransferUserCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookkeeper.transfer_category_id_seq")
    var id: Long?,
    var userId: Long,
    var title: String,
    var description: String?,
    @Version
    var version: Int
) : BaseEntity<TransferUserCategory> {

    override fun toModel(): TransferUserCategory = TransferUserCategory(
        id = requireNotNull(id).asId(),
        userId = userId.asId(),
        title = title,
        description = description,
        version = com.marsofandrew.bookkeeper.base.model.Version(version)
    )
}

internal fun TransferUserCategory.toSpendingUserCategoryEntity() = TransferUserCategoryEntity(
    id = id.rawValue,
    userId = userId.value,
    title = title,
    description = description,
    version = version.value
)
