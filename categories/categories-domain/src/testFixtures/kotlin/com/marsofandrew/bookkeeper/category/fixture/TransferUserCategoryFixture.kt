package com.marsofandrew.bookkeeper.category.fixture

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.category.TransferUserCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId

data class TransferUserCategoryFixture(val id: NumericId<TransferUserCategory>) {

    var userId: NumericId<User> = 5.asId()
    var title: String = "title"
    var description: String? = null
    var version: Version = Version(0)

    fun build() = TransferUserCategory(
        id = id,
        userId = userId,
        title = title,
        description = description,
        version = version
    )
}
