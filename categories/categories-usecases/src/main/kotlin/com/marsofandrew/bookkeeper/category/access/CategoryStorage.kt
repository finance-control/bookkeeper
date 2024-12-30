package com.marsofandrew.bookkeeper.category.access

import com.marsofandrew.bookkeeper.base.exception.orElseThrow
import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.category.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CategoryStorage {
    fun findAllByUserId(userId: NumericId<User>): List<UserCategory>
    fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Set<NumericId<UserCategory>>): List<UserCategory>
    fun findByUserIdAndIdOrThrow(userId: NumericId<User>, id: NumericId<UserCategory>): UserCategory =
        findByUserIdAndId(userId, id).orElseThrow(id)

    fun findByUserIdAndId(userId: NumericId<User>, id: NumericId<UserCategory>): UserCategory?
    fun findByUserIdAndCategoryTitleOrThrow(userId: NumericId<User>, title: String): UserCategory =
        findByUserIdAndCategoryTitle(userId, title).orElseThrow("title", title)

    fun findByUserIdAndCategoryTitle(userId: NumericId<User>, title: String): UserCategory?

    fun existsByUserIdAndCategoryId(userId: NumericId<User>, id: NumericId<UserCategory>): Boolean
    fun existsByUserIdAndCategoryTitle(userId: NumericId<User>, title: String): Boolean

    fun create(userCategory: UserCategory): UserCategory
    fun update(userCategory: UserCategory): UserCategory
    fun delete(ids: Set<NumericId<UserCategory>>)
}