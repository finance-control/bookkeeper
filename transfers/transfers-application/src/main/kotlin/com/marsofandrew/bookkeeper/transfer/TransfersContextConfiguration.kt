package com.marsofandrew.bookkeeper.transfer

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.impl.TransferAddingImpl
import com.marsofandrew.bookkeeper.transfer.impl.TransfersReportCreationImpl
import com.marsofandrew.bookkeeper.transfer.impl.TransferDeletionImpl
import com.marsofandrew.bookkeeper.transfer.impl.TransferSelectionImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TransfersContextConfiguration {

    @Bean
    fun transferAdding(
        transferStorage: TransferStorage,
        eventPublisher: EventPublisher,
    ): TransferAdding = TransferAddingImpl(transferStorage, eventPublisher)

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