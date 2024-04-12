package com.marsofandrew.bookkeeper.transfer.access.entity

import com.marsofandrew.bookkeeper.data.BaseEntity
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.AccountMoney
import com.marsofandrew.bookkeeper.transfer.Transfer
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
@Table(name = "transfer")
internal data class TransferEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, allocationSize = ALLOCATION_SIZE)
    var id: Long?,
    var userId: Long,
    var date: LocalDate,
    var description: String,
    var sendAmount: BigDecimal?,
    var receivedAmount: BigDecimal,
    @Enumerated(EnumType.STRING)
    var sendCurrency: Currency?,
    @Enumerated(EnumType.STRING)
    var receivedCurrency: Currency,
    var sourceAccountId: String?,
    var destinationAccountId: String?,
    val categoryId: Long,
    var createdAt: LocalDate,
    @Version
    var version: Int
) : BaseEntity<Transfer> {

    override fun toModel() = Transfer(
        id = requireNotNull(id).asId(),
        userId = userId.asId(),
        date = date,
        send = sendAmount?.let {
            AccountMoney(
                money = PositiveMoney(requireNotNull(sendCurrency) { "sendCurrency is null" }, it),
                accountId = sourceAccountId?.asId()
            )
        },
        received = AccountMoney(
            money = PositiveMoney(receivedCurrency, receivedAmount),
            accountId = destinationAccountId?.asId()
        ),
        description = description,
        categoryId = categoryId.asId(),
        createdAt = createdAt,
        version = com.marsofandrew.bookkeeper.base.model.Version(version)
    )

    companion object {
        const val ALLOCATION_SIZE = 1000
        const val SEQUENCE_NAME = "transfer_id_seq"
    }
}

internal fun Transfer.toTransferEntity() = TransferEntity(
    id = id.rawValue,
    userId = userId.value,
    date = date,
    description = description,
    sendAmount = send?.money?.amount,
    receivedAmount = received.money.amount,
    sendCurrency = send?.money?.currency,
    receivedCurrency = received.money.currency,
    sourceAccountId = send?.accountId?.value,
    destinationAccountId = received.accountId?.value,
    categoryId = categoryId.value,
    createdAt = createdAt,
    version = version.value
)
