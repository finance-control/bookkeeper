package com.marsofandrew.bookkeeper.spending.access.entity

import com.marsofandrew.bookkeeper.data.BaseEntity
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.Spending
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "spending")
internal data class SpendingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, allocationSize = ALLOCATION_SIZE)
    var id: Long?,
    var userId: Long,
    var date: LocalDate,
    var description: String,
    var amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    var currency: Currency,
    val categoryId: Long,
    var createdAt: LocalDate,
    var fromAccount: String?,
    @Version
    var version: Int
) : BaseEntity<Spending> {

    override fun toModel(): Spending = Spending(
        id = requireNotNull(id).asId(),
        userId = userId.asId(),
        date = date,
        description = description,
        money = PositiveMoney(currency, amount),
        spendingCategoryId = categoryId.asId(),
        createdAt = createdAt,
        fromAccount = fromAccount?.asId(),
        version = com.marsofandrew.bookkeeper.base.model.Version(version)
    )

    companion object {
        const val ALLOCATION_SIZE = 1000
        const val SEQUENCE_NAME = "spending_id_seq"
    }
}

internal fun Spending.toSpendingEntity() = SpendingEntity(
    id = id.rawValue,
    userId = userId.value,
    date = date,
    description = description,
    amount = money.amount,
    currency = money.currency,
    categoryId = spendingCategoryId.value,
    createdAt = createdAt,
    fromAccount = fromAccount?.value,
    version = version.value
)
