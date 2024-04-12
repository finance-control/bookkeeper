package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingAdding
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.account.SpendingAccountValidator
import com.marsofandrew.bookkeeper.spending.category.SpendingCategoryValidator
import com.marsofandrew.bookkeeper.spending.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.spending.exception.InvalidCategoryException
import org.apache.logging.log4j.LogManager

class SpendingAddingImpl(
    private val spendingStorage: SpendingStorage,
    private val eventPublisher: EventPublisher,
    private val spendingCategoryValidator: SpendingCategoryValidator,
    private val spendingAccountValidator: SpendingAccountValidator
) : SpendingAdding {

    private val logger = LogManager.getLogger()

    override fun add(spending: Spending): Spending {
        if (!spendingCategoryValidator.validate(spending.userId, spending.categoryId)) {
            throw InvalidCategoryException(spending.categoryId)
        }

        if (spending.sourceAccountId?.let { spendingAccountValidator.validate(spending.userId, it) } == false) {
            throw InvalidAccountException(spending.sourceAccountId!!)
        }

        val createdSpending = spendingStorage.create(spending)
        logger.info("Spending with id ${createdSpending.id} was created")

        eventPublisher.publish(createdSpending.toMoneyIsSpendEvent())
        return createdSpending
    }
}

private fun Spending.toMoneyIsSpendEvent() = MoneyIsSpendEvent(
    userId = userId.value,
    date = date,
    money = AccountBondedMoney(money, sourceAccountId?.value),
    category = categoryId.value
)
