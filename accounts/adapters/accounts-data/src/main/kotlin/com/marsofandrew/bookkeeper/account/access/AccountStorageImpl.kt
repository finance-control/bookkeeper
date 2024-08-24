package com.marsofandrew.bookkeeper.account.access

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.access.entity.toAccountEntity
import com.marsofandrew.bookkeeper.account.access.repository.AccountRepository
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.exception.orElseThrow
import com.marsofandrew.bookkeeper.data.toModelsSet
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import io.seruco.encoding.base62.Base62
import java.nio.ByteBuffer
import java.time.Clock
import java.time.LocalDate
import java.util.Objects
import java.util.concurrent.ThreadLocalRandom
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
internal class AccountStorageImpl(
    private val accountRepository: AccountRepository,
    private val clock: Clock,
    private val threadLocalRandom: ThreadLocalRandom = ThreadLocalRandom.current()
) : AccountStorage {

    override fun findAllByUserId(userId: NumericId<User>): Set<Account> {
        return accountRepository.findAllByUserId(userId.value).toModelsSet()
    }

    override fun findByUserIdAndIdOrThrow(userId: NumericId<User>, id: StringId<Account>): Account {
        return accountRepository.findByUserIdAndId(userId.value, id.value).orElseThrow(id).toModel()
    }

    override fun existsByUserIdAndAccountId(userId: NumericId<User>, id: StringId<Account>): Boolean {
        return accountRepository.existsByUserIdAndId(userId.value, id.value)
    }

    override fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Set<StringId<Account>>): Set<Account> {
        return accountRepository.findAllByUserIdAndIdIn(userId.value, ids.map { it.value }).toModelsSet()
    }

    override fun findAccountsForRemoval(limit: Int): Set<Account> {
        return accountRepository.findAllByStatusEquals(Account.Status.FOR_REMOVAL).toModelsSet()
    }

    override fun create(account: Account): Account {
        validateFiled(!account.id.initialized) { "Attempt to create identified account" }
        val accountForSaving = account.identify()

        return accountRepository.saveAndFlush(accountForSaving.toAccountEntity()).toModel()
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun setMoney(id: StringId<Account>, money: Money) {
        val account = accountRepository.findById(id.value).getOrNull().orElseThrow(id)

        require(account.currency == money.currency) {
            "Account currency is ${account.currency} while updating currency is: ${money.currency}"
        }

        val updatedAccount = account.copy(amount = money.amount)

        accountRepository.saveAndFlush(updatedAccount)
    }


    @Transactional(propagation = Propagation.REQUIRED)
    override fun setForRemovalAndClose(ids: Set<StringId<Account>>, closedAt: LocalDate) {
        accountRepository.setForRemovalAndClose(ids.map { it.value }, closedAt)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun delete(ids: Set<StringId<Account>>) {
        accountRepository.deleteAllById(ids.map { it.value })
    }

    private fun Account.identify(): Account {
        val encoder = Base62.createInstance()
        val encodedUserId = encoder.encode(userId.value.toBytes())
        val encodedUniquePart =
            encoder.encode(Objects.hash(clock.instant(), title, openedAt, threadLocalRandom.nextInt()).toBytes())

        return copy(id = "$VERSION-${String(encodedUserId)}-${String(encodedUniquePart)}".asId())
    }

    private fun Int.toBytes(): ByteArray =
        ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this).array()

    private fun Long.toBytes(): ByteArray =
        ByteBuffer.allocate(Long.SIZE_BYTES).putLong(this).array()

    private companion object {
        const val VERSION = "v1"
    }
}
