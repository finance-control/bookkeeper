package com.marsofandrew.bookkeeper.transfer

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfer.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfer.impl.TransferAddingImpl
import com.marsofandrew.bookkeeper.transfer.impl.TransferDeletionImpl
import com.marsofandrew.bookkeeper.transfer.impl.TransferSelectionImpl
import com.marsofandrew.bookkeeper.transfer.impl.TransfersReportCreationImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TransfersContextConfiguration {

    @Bean
    fun transferAdding(
        transferStorage: TransferStorage,
        eventPublisher: EventPublisher,
        transferAccountValidator: TransferAccountValidator,
        transferCategoryValidator: TransferCategoryValidator
    ): TransferAdding = TransferAddingImpl(
        transferStorage,
        eventPublisher,
        transferAccountValidator,
        transferCategoryValidator
    )

    @Bean
    fun transferReportCreation(
        transferStorage: TransferStorage
    ): TransferReportCreation = TransfersReportCreationImpl(transferStorage)

    @Bean
    fun transferDeletion(
        transferStorage: TransferStorage,
        eventPublisher: EventPublisher,
    ): TransferDeletion = TransferDeletionImpl(transferStorage, eventPublisher)

    @Bean
    fun transferSelection(
        transferStorage: TransferStorage
    ): TransferSelection = TransferSelectionImpl(transferStorage)
}