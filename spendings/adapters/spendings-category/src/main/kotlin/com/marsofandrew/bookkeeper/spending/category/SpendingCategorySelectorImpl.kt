package com.marsofandrew.bookkeeper.spending.category

import com.marsofandrew.bookkeeper.category.CategorySelection
import com.marsofandrew.bookkeeper.category.UserCategory
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.user.User
import org.springframework.stereotype.Service

@Service
internal class SpendingCategorySelectorImpl(
    private val categorySelection: CategorySelection,
) : SpendingCategorySelector {
    override fun select(
        userId: NumericId<User>,
        categoryId: NumericId<Category>
    ): Category {
        return categorySelection.select(userId.value.asId(), setOf(categoryId.value.asId()))
            .first()
            .toCategory()
    }

    override fun selectAllByIds(
        userId: NumericId<User>,
        ids: List<NumericId<Category>>
    ): List<Category> {
        return categorySelection.select(userId.value.asId(), ids.map { it.value.asId<UserCategory>() }.toSet())
            .map { it.toCategory() }
    }


}

private fun UserCategory.toCategory() = Category(
    id = id.value.asId(),
    title = title,
    description = description,
)
