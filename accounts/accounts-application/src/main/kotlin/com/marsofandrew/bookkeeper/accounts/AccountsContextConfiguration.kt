package com.marsofandrew.bookkeeper.accounts

import com.marsofandrew.bookkeeper.accounts.access.AccountStorage
import com.marsofandrew.bookkeeper.accounts.impl.AccountCleanupImpl
import com.marsofandrew.bookkeeper.accounts.impl.AccountCreationImpl
import com.marsofandrew.bookkeeper.accounts.impl.AccountDeletionImpl
import com.marsofandrew.bookkeeper.accounts.impl.AccountMoneySpendingImpl
import com.marsofandrew.bookkeeper.accounts.impl.AccountMoneyTransferringImpl
import com.marsofandrew.bookkeeper.accounts.impl.AccountSelectionImpl
import com.marsofandrew.bookkeeper.accounts.impl.RollbackAccountMoneySpendingImpl
import com.marsofandrew.bookkeeper.accounts.impl.RollbackAccountMoneyTransferringImpl
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
    ): AccountCleanup = AccountCleanupImpl(accountStorage, clock, { 1000 }) { months >= 1 }

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