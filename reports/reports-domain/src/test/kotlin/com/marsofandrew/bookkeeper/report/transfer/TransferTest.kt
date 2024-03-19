package com.marsofandrew.bookkeeper.report.transfer

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.Test

internal class TransferTest {

    @Test
    fun `constructor throws exception when date is before 2022-01-01`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            Transfer(
                userId = 45.asId(),
                date = LocalDate.of(2021, 12, 25),
                send = PositiveMoney(Currency.EUR, 54, 1),
                received = PositiveMoney(Currency.EUR, 54, 1),
                categoryId = 45.asId()
            )
        }
    }

    @Test
    fun `constructor when correct values are set creates the object`() {
        val userId = 45
        val date = LocalDate.of(2022, 12, 25)
        val send = PositiveMoney(Currency.EUR, 54, 1)
        val received = PositiveMoney(Currency.USD, 54, 1)
        val categoryId = 875

        val transfer = Transfer(
            userId = userId.asId(),
            date = date,
            send = send,
            received = received,
            categoryId = categoryId.asId()
        )

        transfer.userId.value shouldBe userId
        transfer.date shouldBe date
        transfer.send shouldBe send
        transfer.received shouldBe received
        transfer.categoryId.value shouldBe categoryId
    }
}
