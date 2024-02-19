package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.impl.TransferAddingImpl
import com.marsofandrew.bookkeeper.transfers.impl.TransfersReportCreationImpl
import com.marsofandrew.bookkeeper.transfers.impl.TransferDeletionImpl
import com.marsofandrew.bookkeeper.transfers.impl.TransferSelectionImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TransfersContextConfiguration {

    @Bean
    fun transferAdding(
        transferStorage: TransferStorage
    ): TransferAdding = TransferAddingImpl(transferStorage)

    @Bean
    fun transferReportCreation(
        transferStorage: TransferStorage
    ): TransferReportCreation = TransfersReportCreationImpl(transferStorage)

    @Bean
    fun transferDeletion(
        transferStorage: TransferStorage
    ): TransferDeletion = TransferDeletionImpl(transferStorage)

    @Bean
    fun transferSelection(
        transferStorage: TransferStorage
    ): TransferSelection = TransferSelectionImpl(transferStorage)
}