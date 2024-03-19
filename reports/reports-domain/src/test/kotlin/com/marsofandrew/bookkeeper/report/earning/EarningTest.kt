package com.marsofandrew.bookkeeper.report.earning

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.Test

internal class EarningTest {

    @Test
    fun `constructor throws exception when date is before 2022-01-01`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            Earning(
                userId = 45.asId(),
                date = LocalDate.of(2021, 12, 25),
                money = PositiveMoney(Currency.EUR, 54, 1),
                categoryId = 45.asId()
            )
        }
    }

    @Test
    fun `constructor when correct values are set creates the object`() {
        val userId = 45
        val date = LocalDate.of(2022, 12, 25)
        val money = PositiveMoney(Currency.EUR, 54, 1)
        val categoryId = 875

        val earning = Earning(
            userId = userId.asId(),
            date = date,
            money = money,
            categoryId = categoryId.asId()
        )

        earning.userId.value shouldBe userId
        earning.date shouldBe date
        earning.money shouldBe money
        earning.categoryId.value shouldBe categoryId
    }
}
