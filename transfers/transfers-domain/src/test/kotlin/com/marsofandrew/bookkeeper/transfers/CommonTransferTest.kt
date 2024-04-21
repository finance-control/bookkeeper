package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.Test

internal class CommonTransferTest {

    @Test
    fun `constructor throws exception when date is before 2022-01-01`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            CommonTransfer(
                id = 5.asId(),
                userId = 45.asId(),
                date = LocalDate.of(2021, 12, 30),
                send = null,
                received = AccountMoney(money = PositiveMoney(Currency.EUR, 15, 1), accountId = null),
                description = "tes",
                categoryId = 875.asId(),
                createdAt = LocalDate.now(),
                version = Version(0)
            )
        }
    }

    @Test
    fun `constructor throws exception when createdAt  is before 2024-01-01`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            CommonTransfer(
                id = 5.asId(),
                userId = 45.asId(),
                date = LocalDate.now(),
                send = null,
                received = AccountMoney(money = PositiveMoney(Currency.EUR, 15, 1), accountId = null),
                description = "tes",
                categoryId = 875.asId(),
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

        val commonTransfer = CommonTransfer(
            id = id.asId(),
            userId = userId.asId(),
            date = date,
            send = null,
            received = received,
            description = description,
            categoryId = categoryId.asId(),
            createdAt = date,
            version = Version(0)
        )

        commonTransfer.id.value shouldBe id
        commonTransfer.userId.value shouldBe userId
        commonTransfer.date shouldBe date
        commonTransfer.send shouldBe null
        commonTransfer.received shouldBe received
        commonTransfer.description shouldBe description
        commonTransfer.categoryId.value shouldBe categoryId
        commonTransfer.createdAt shouldBe date
    }
}
