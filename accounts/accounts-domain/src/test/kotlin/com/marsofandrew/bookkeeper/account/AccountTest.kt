package com.marsofandrew.bookkeeper.account

import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class AccountTest {

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "\t"])
    fun `constructor throws exception when empty title is provided`(title: String) {
        shouldThrowExactly<IllegalStateException> {
            Account(
                id = StringId.unidentified(),
                userId = 5.asId(),
                money = Money(Currency.EUR, 10, 1),
                title = title,
                openedAt = LocalDate.now(),
                closedAt = null,
                status = Account.Status.IN_USE,
                version = Version(0)
            )
        }
    }

    @Test
    fun `constructor creates Account when correct values are set`() {
        val id = "ttaa".asId<Account>()
        val userId = 5.asId<User>()
        val money = Money(Currency.EUR, 10, 1)
        val title = "tts"
        val openedAt = LocalDate.now()
        val status = Account.Status.IN_USE

        val account = Account(
            id = id,
            userId = userId,
            money = money,
            title = title,
            openedAt = openedAt,
            closedAt = null,
            status = status,
            version = Version(0)

        )

        account.id shouldBe id
        account.userId shouldBe userId
        account.money shouldBe money
        account.title shouldBe title
        account.openedAt shouldBe openedAt
        account.status shouldBe status
    }

    @Test
    fun `topUp when status is correct returns new account with increased money`(){
        val account = Account(
            id = StringId.unidentified(),
            userId = 5.asId(),
            money = Money(Currency.EUR, 10, 1),
            title = "title",
            openedAt = LocalDate.now(),
            closedAt = null,
            status = Account.Status.IN_USE,
            version = Version(0)
        )
        val updatedAccount = account.topUp(PositiveMoney(Currency.EUR, 10, 0))

        updatedAccount.money shouldBe Money(Currency.EUR, 11, 0)
    }

    @Test
    fun `topUp throws exception when account is set for removal`(){
        val account = Account(
            id = StringId.unidentified(),
            userId = 5.asId(),
            money = Money(Currency.EUR, 10, 1),
            title = "title",
            openedAt = LocalDate.now(),
            closedAt = null,
            status = Account.Status.FOR_REMOVAL,
            version = Version(0)
        )
        shouldThrowExactly<IllegalStateException> {
            account.topUp(PositiveMoney(Currency.EUR, 10, 0))
        }
    }

    @Test
    fun `topUp throws exception when account is closed`(){
        val account = Account(
            id = StringId.unidentified(),
            userId = 5.asId(),
            money = Money(Currency.EUR, 10, 1),
            title = "title",
            openedAt = LocalDate.now().minusDays(1),
            closedAt = LocalDate.now(),
            status = Account.Status.IN_USE,
            version = Version(0)
        )
        shouldThrowExactly<IllegalStateException> {
            account.topUp(PositiveMoney(Currency.EUR, 10, 0))
        }
    }

    @Test
    fun `withdraw when status is correct returns new account with decreased money`(){
        val account = Account(
            id = StringId.unidentified(),
            userId = 5.asId(),
            money = Money(Currency.EUR, 10, 1),
            title = "title",
            openedAt = LocalDate.now(),
            closedAt = null,
            status = Account.Status.IN_USE,
            version = Version(0)
        )
        val updatedAccount = account.withdraw(PositiveMoney(Currency.EUR, 10, 0))

        updatedAccount.money shouldBe Money(Currency.EUR, -9, 0)
    }

    @Test
    fun `withdraw throws exception when account is set for removal`(){
        val account = Account(
            id = StringId.unidentified(),
            userId = 5.asId(),
            money = Money(Currency.EUR, 10, 1),
            title = "title",
            openedAt = LocalDate.now(),
            closedAt = null,
            status = Account.Status.FOR_REMOVAL,
            version = Version(0)
        )
        shouldThrowExactly<IllegalStateException> {
            account.withdraw(PositiveMoney(Currency.EUR, 10, 0))
        }
    }

    @Test
    fun `withdraw throws exception when account is closed`(){
        val account = Account(
            id = StringId.unidentified(),
            userId = 5.asId(),
            money = Money(Currency.EUR, 10, 1),
            title = "title",
            openedAt = LocalDate.now().minusDays(1),
            closedAt = LocalDate.now(),
            status = Account.Status.IN_USE,
            version = Version(0)
        )
        shouldThrowExactly<IllegalStateException> {
            account.withdraw(PositiveMoney(Currency.EUR, 10, 0))
        }
    }
}