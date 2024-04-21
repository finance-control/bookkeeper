package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.account.TransferAccountValidator
import com.marsofandrew.bookkeeper.transfers.category.TransferCategoryValidator
import com.marsofandrew.bookkeeper.transfers.earning.EarningAdding
import com.marsofandrew.bookkeeper.transfers.earning.EarningReportCreation
import com.marsofandrew.bookkeeper.transfers.earning.EarningSelection
import com.marsofandrew.bookkeeper.transfers.impl.commonTransfer.CommonTransferDeletionImpl
import com.marsofandrew.bookkeeper.transfers.impl.commonTransfer.CommonTransferSelectionImpl
import com.marsofandrew.bookkeeper.transfers.impl.commonTransfer.CommonTransfersReportCreationImpl
import com.marsofandrew.bookkeeper.transfers.impl.earning.EarningAddingImpl
import com.marsofandrew.bookkeeper.transfers.impl.earning.EarningReportCreationImpl
import com.marsofandrew.bookkeeper.transfers.impl.earning.EarningSelectionImpl
import com.marsofandrew.bookkeeper.transfers.impl.transfer.TransferAddingImpl
import com.marsofandrew.bookkeeper.transfers.impl.transfer.TransferReportCreationImpl
import com.marsofandrew.bookkeeper.transfers.impl.transfer.TransferSelectionImpl
import com.marsofandrew.bookkeeper.transfers.transfer.TransferAdding
import com.marsofandrew.bookkeeper.transfers.transfer.TransferReportCreation
import com.marsofandrew.bookkeeper.transfers.transfer.TransferSelection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TransfersContextConfiguration {

    @Bean
    fun commonTransferReportCreation(
        transferStorage: TransferStorage
    ): CommonTransferReportCreation = CommonTransfersReportCreationImpl(transferStorage)

    @Bean
    fun commonTransferDeletion(
        transferStorage: TransferStorage,
        eventPublisher: EventPublisher,
    ): CommonTransferDeletion = CommonTransferDeletionImpl(transferStorage, eventPublisher)

    @Bean
    fun commonTransferSelection(
        transferStorage: TransferStorage
    ): CommonTransferSelection = CommonTransferSelectionImpl(transferStorage)

    @Bean
    fun earningAdding(
        transferStorage: TransferStorage,
        eventPublisher: EventPublisher,
        transferAccountValidator: TransferAccountValidator,
        transferCategoryValidator: TransferCategoryValidator
    ): EarningAdding = EarningAddingImpl(
        transferStorage,
        eventPublisher,
        transferAccountValidator,
        transferCategoryValidator
    )

    @Bean
    fun earningReportCreation(
        transferStorage: TransferStorage
    ): EarningReportCreation = EarningReportCreationImpl(transferStorage)

    @Bean
    fun earningSelection(
        transferStorage: TransferStorage
    ): EarningSelection = EarningSelectionImpl(transferStorage)

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
    ): TransferReportCreation = TransferReportCreationImpl(transferStorage)

    @Bean
    fun transferSelection(
        transferStorage: TransferStorage
    ): TransferSelection = TransferSelectionImpl(transferStorage)
}