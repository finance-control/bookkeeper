package com.marsofandrew.bookkeeper.account

import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.impl.AccountCleanupImpl
import com.marsofandrew.bookkeeper.account.impl.AccountCreationImpl
import com.marsofandrew.bookkeeper.account.impl.AccountDeletionImpl
import com.marsofandrew.bookkeeper.account.impl.AccountMoneySpendingImpl
import com.marsofandrew.bookkeeper.account.impl.AccountMoneyTransferringImpl
import com.marsofandrew.bookkeeper.account.impl.AccountSelectionImpl
import com.marsofandrew.bookkeeper.account.impl.RollbackAccountMoneySpendingImpl
import com.marsofandrew.bookkeeper.account.impl.RollbackAccountMoneyTransferringImpl
import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import java.time.Clock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class AccountsContextConfiguration {

    @Bean
    fun accountCleanup(
        accountStorage: AccountStorage,
        clock: Clock
    ): AccountCleanup = AccountCleanupImpl(accountStorage, clock) { months >= 1 }

    @Bean
    fun accountCreation(accountStorage: AccountStorage): AccountCreation = AccountCreationImpl(accountStorage)

    @Bean
    fun accountDeletion(
        accountStorage: AccountStorage,
        clock: Clock
    ): AccountDeletion = AccountDeletionImpl(accountStorage, clock)

    @Bean
    fun accountMoneySpending(
        accountStorage: AccountStorage,
        transactionalExecution: TransactionalExecution,
    ): AccountMoneySpending = AccountMoneySpendingImpl(accountStorage, transactionalExecution)

    @Bean
    fun accountMoneyTransferring(
        accountStorage: AccountStorage,
        transactionalExecution: TransactionalExecution,
    ): AccountMoneyTransferring = AccountMoneyTransferringImpl(accountStorage, transactionalExecution)

    @Bean
    fun rollbackAccountMoneySpending(
        accountStorage: AccountStorage,
        transactionalExecution: TransactionalExecution,
    ): RollbackAccountMoneySpending = RollbackAccountMoneySpendingImpl(accountStorage, transactionalExecution)

    @Bean
    fun rollbackAccountMoneyTransferring(
        accountStorage: AccountStorage,
        transactionalExecution: TransactionalExecution,
    ): RollbackAccountMoneyTransferring = RollbackAccountMoneyTransferringImpl(accountStorage, transactionalExecution)

    @Bean
    fun accountSelection(
        accountStorage: AccountStorage
    ): AccountSelection = AccountSelectionImpl(accountStorage)
}