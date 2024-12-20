package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.account.Account
import com.marsofandrew.bookkeeper.spending.category.Category
import com.marsofandrew.bookkeeper.spending.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.Test

internal class SpendingTest {

    @Test
    fun `constructor throws exception when createdAt is before 2024-01-01`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            Spending(
                id = NumericId.unidentified(),
                userId = NumericId.unidentified(),
                money = PositiveMoney(Currency.EUR, 10),
                date = LocalDate.now(),
                description = "dsd",
                categoryId = 465.asId(),
                createdAt = LocalDate.of(2023, 12, 20),
                sourceAccountId = null,
                version = Version(0)
            )
        }
    }

    @Test
    fun `constructor throws exception when date is before 2022-01-01`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            Spending(
                id = NumericId.unidentified(),
                userId = NumericId.unidentified(),
                money = PositiveMoney(Currency.EUR, 10),
                date = LocalDate.of(2021, 12, 20),
                description = "dsd",
                categoryId = 465.asId(),
                createdAt = LocalDate.of(2024, 12, 20),
                sourceAccountId = null,
                version = Version(0)
            )
        }
    }

    @Test
    fun `constructor when correct values are set then creates spending`() {
        val id = NumericId.unidentified<Spending>()
        val userId = 465.asId<User>()
        val money = PositiveMoney(Currency.EUR, 10)
        val date = LocalDate.of(2023, 12, 20)
        val description = "dhjw"
        val categoryId = 465.asId<Category>()
        val createdAt = LocalDate.of(2024, 12, 20)
        val sourceAccountId = "test".asId<Account>()

        val result = Spending(
            id = id,
            userId = userId,
            money = money,
            date = date,
            description = description,
            categoryId = categoryId,
            createdAt = createdAt,
            sourceAccountId = sourceAccountId,
            version = Version(0)
        )

        result.id shouldBe id
        result.userId shouldBe userId
        result.money shouldBe money
        result.date shouldBe date
        result.description shouldBe description
        result.categoryId shouldBe categoryId
        result.createdAt shouldBe createdAt
        result.sourceAccountId shouldBe sourceAccountId
    }
}
