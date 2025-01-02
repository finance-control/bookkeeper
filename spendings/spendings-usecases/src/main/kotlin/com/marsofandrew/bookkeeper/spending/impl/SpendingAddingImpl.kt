package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingAdding
import com.marsofandrew.bookkeeper.spending.SpendingWithCategory
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.account.SpendingAccountValidator
import com.marsofandrew.bookkeeper.spending.category.SpendingCategorySelector
import com.marsofandrew.bookkeeper.spending.category.SpendingCategoryValidator
import com.marsofandrew.bookkeeper.spending.impl.util.SpendingValidator
import com.marsofandrew.bookkeeper.spending.impl.util.toMoneyIsSpendEvent
import org.apache.logging.log4j.LogManager

class SpendingAddingImpl(
    private val spendingStorage: SpendingStorage,
    private val eventPublisher: EventPublisher,
    private val spendingCategorySelector: SpendingCategorySelector,
    spendingCategoryValidator: SpendingCategoryValidator,
    spendingAccountValidator: SpendingAccountValidator
) : SpendingAdding {
    private val validator = SpendingValidator(spendingCategoryValidator, spendingAccountValidator)
    private val logger = LogManager.getLogger()

    override fun add(spending: Spending): SpendingWithCategory {
        validator.validate(spending)
        val createdSpending = spendingStorage.create(spending)
        logger.info("Spending with id ${createdSpending.id} was created")

        eventPublisher.publish(createdSpending.toMoneyIsSpendEvent())

        val category = spendingCategorySelector.select(spending.userId, spending.categoryId)
        return SpendingWithCategory(
            spending = createdSpending,
            category = category,
        )
    }
}
