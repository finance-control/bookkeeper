package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.event.MoneyIsSpendEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingAdding
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.account.AccountValidator
import com.marsofandrew.bookkeeper.spending.category.CategoryValidator
import com.marsofandrew.bookkeeper.spending.exception.InvalidAccountException
import com.marsofandrew.bookkeeper.spending.exception.InvalidCategoryException

class SpendingAddingImpl(
    private val spendingStorage: SpendingStorage,
    private val eventPublisher: EventPublisher,
    private val categoryValidator: CategoryValidator,
    private val accountValidator: AccountValidator
) : SpendingAdding {

    override fun add(spending: Spending): Spending {
        if (!categoryValidator.validate(spending.userId, spending.spendingCategoryId)) {
            throw InvalidCategoryException(spending.spendingCategoryId)
        }

        if (spending.fromAccount?.let { accountValidator.validate(spending.userId, it) } == false) {
            throw InvalidAccountException(spending.fromAccount!!)
        }

        val createdSpending = spendingStorage.create(spending)
        eventPublisher.publish(createdSpending.toMoneyIsSpendEvent())
        return createdSpending
    }
}

private fun Spending.toMoneyIsSpendEvent() = MoneyIsSpendEvent(
    userId = userId.value,
    date = date,
    money = AccountBondedMoney(money, fromAccount?.value),
    category = spendingCategoryId.value
)
