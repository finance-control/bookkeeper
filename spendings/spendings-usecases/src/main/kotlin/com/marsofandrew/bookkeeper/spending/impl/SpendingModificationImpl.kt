package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingModification
import com.marsofandrew.bookkeeper.spending.SpendingUpdate
import com.marsofandrew.bookkeeper.spending.SpendingWithCategory
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.account.SpendingAccountValidator
import com.marsofandrew.bookkeeper.spending.category.CategorySelector
import com.marsofandrew.bookkeeper.spending.category.SpendingCategoryValidator
import com.marsofandrew.bookkeeper.spending.impl.util.SpendingValidator
import com.marsofandrew.bookkeeper.spending.impl.util.toMoneyIsSpendEvent
import com.marsofandrew.bookkeeper.spending.impl.util.toRollbackMoneyIsSendEvent
import com.marsofandrew.bookkeeper.spending.toSpending
import com.marsofandrew.bookkeeper.spending.user.User
import org.apache.logging.log4j.LogManager

class SpendingModificationImpl(
    private val spendingStorage: SpendingStorage,
    private val eventPublisher: EventPublisher,
    private val transactionExecutor: TransactionExecutor,
    private val categorySelector: CategorySelector,
    spendingCategoryValidator: SpendingCategoryValidator,
    spendingAccountValidator: SpendingAccountValidator
) : SpendingModification {

    private val validator = SpendingValidator(spendingCategoryValidator, spendingAccountValidator)
    private val logger = LogManager.getLogger()

    override fun modify(userId: NumericId<User>, modification: SpendingUpdate): SpendingWithCategory = transactionExecutor.execute {
        val originalSpending = spendingStorage.findByIdAndUserIdOrThrow(modification.id, userId)
        val updatedSpending = modification.toSpending(originalSpending)
        validator.validate(updatedSpending)

        spendingStorage.update(updatedSpending)

        logger.info("Spending ${originalSpending.id} has been updated")
        eventPublisher.publish(originalSpending.toRollbackMoneyIsSendEvent())
        eventPublisher.publish(updatedSpending.toMoneyIsSpendEvent())

        val category = categorySelector.select(userId, updatedSpending.categoryId)

        SpendingWithCategory(
            spending = updatedSpending,
            category = category,
        )
    }
}