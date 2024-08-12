package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.account.Account
import com.marsofandrew.bookkeeper.spending.category.Category
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate


internal class SpendingUpdateTest {

    @Test
    fun `constructor throws exception when date is before 2022-01-01`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            SpendingUpdate(
                id = NumericId.unidentified(),
                money = PositiveMoney(Currency.EUR, 10),
                date = LocalDate.of(2021, 12, 20),
                description = "dsd",
                categoryId = 465.asId(),
                sourceAccountId = null,
                version = Version(0)
            )
        }
    }

    @Test
    fun `constructor throws exception when categoryId is uninitialised`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            SpendingUpdate(
                id = NumericId.unidentified(),
                money = PositiveMoney(Currency.EUR, 10),
                date = LocalDate.of(2021, 12, 20),
                description = "dsd",
                categoryId = NumericId.unidentified(),
                sourceAccountId = null,
                version = Version(0)
            )
        }
    }


    @Test
    fun `constructor when correct values are set then creates spending`() {
        val id = NumericId.unidentified<Spending>()
        val money = PositiveMoney(Currency.EUR, 10)
        val date = LocalDate.of(2023, 12, 20)
        val description = "dhjw"
        val categoryId = 465.asId<Category>()
        val sourceAccountId = "test".asId<Account>()

        val result = SpendingUpdate(
            id = id,
            money = money,
            date = date,
            description = description,
            categoryId = categoryId,
            sourceAccountId = sourceAccountId,
            version = Version(0)
        )

        result.id shouldBe id
        result.money shouldBe money
        result.date shouldBe date
        result.description shouldBe description
        result.categoryId shouldBe categoryId
        result.sourceAccountId shouldBe sourceAccountId
    }

}