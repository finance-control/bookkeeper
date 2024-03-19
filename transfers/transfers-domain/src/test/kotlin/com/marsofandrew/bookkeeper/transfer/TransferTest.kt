package com.marsofandrew.bookkeeper.transfer

import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
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
                id = 5.asId(),
                userId = 45.asId(),
                date = LocalDate.of(2021, 12, 30),
                send = null,
                received = AccountMoney(money = PositiveMoney(Currency.EUR, 15, 1), accountId = null),
                description = "tes",
                transferCategoryId = 875.asId(),
                createdAt = LocalDate.now(),
                version = Version(0)
            )
        }
    }

    @Test
    fun `constructor throws exception when createdAt  is before 2024-01-01`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            Transfer(
                id = 5.asId(),
                userId = 45.asId(),
                date = LocalDate.now(),
                send = null,
                received = AccountMoney(money = PositiveMoney(Currency.EUR, 15, 1), accountId = null),
                description = "tes",
                transferCategoryId = 875.asId(),
                createdAt = LocalDate.of(2023, 12, 30),
                version = Version(0)
            )
        }
    }


    @Test
    fun `constructor when correct values are passed creates Transfer`() {
        val date = LocalDate.now()
        val received = AccountMoney(money = PositiveMoney(Currency.EUR, 15, 1), accountId = null)
        val id = 5
        val userId = 56876
        val categoryId = 8765
        val description = "tes"

        val transfer = Transfer(
            id = id.asId(),
            userId = userId.asId(),
            date = date,
            send = null,
            received = received,
            description = description,
            transferCategoryId = categoryId.asId(),
            createdAt = date,
            version = Version(0)
        )

        transfer.id.value shouldBe id
        transfer.userId.value shouldBe userId
        transfer.date shouldBe date
        transfer.send shouldBe null
        transfer.received shouldBe received
        transfer.description shouldBe description
        transfer.transferCategoryId.value shouldBe categoryId
        transfer.createdAt shouldBe date
    }
}
