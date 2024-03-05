package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.SpendingDeletion
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingDeletionImplTest {

    private val spendingStorage = mockk<SpendingStorage>(relaxUnitFun = true)
    private val eventPublisher = mockk<EventPublisher>(relaxUnitFun = true)

    private lateinit var spendingDeletion: SpendingDeletion

    @BeforeEach
    fun setup() {
        spendingDeletion = SpendingDeletionImpl(spendingStorage, eventPublisher)
    }

    @Test
    fun `delete deletes spending by ids`() {
        val ids = setOf<NumericId<Spending>>(
            55.asId(),
            45.asId(),
            35.asId(),
        )

        spendingDeletion.delete(1.asId(), ids)

        verify(exactly = 1) { spendingStorage.delete(ids) }
        // verify(exactly = 1) { eventPublisher.publish() }
    }
}