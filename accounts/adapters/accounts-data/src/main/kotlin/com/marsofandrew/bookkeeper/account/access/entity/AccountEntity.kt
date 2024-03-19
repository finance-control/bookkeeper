package com.marsofandrew.bookkeeper.account.access.entity

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.data.BaseEntity
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.asId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "account")
internal data class AccountEntity(
    @Id
    var id: String,
    var userId: Long,
    var amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    var currency: Currency,
    var title: String,
    var openedAt: LocalDate,
    var closedAt: LocalDate?,
    @Enumerated(EnumType.STRING)
    var status: Account.Status,
    @Version
    var version: Int
) : BaseEntity<Account> {

    override fun toModel(): Account = Account(
        id = id.asId(),
        userId = userId.asId(),
        money = Money(currency, amount),
        title = title,
        openedAt = openedAt,
        closedAt = closedAt,
        status = status,
        version = com.marsofandrew.bookkeeper.base.model.Version(version)
    )
}

internal fun Account.toAccountEntity() = AccountEntity(
    id = id.value,
    userId = userId.value,
    amount = money.amount,
    currency = money.currency,
    title = title,
    openedAt = openedAt,
    closedAt = closedAt,
    status = status,
    version = version.value
)
