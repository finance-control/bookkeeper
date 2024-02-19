package com.marsofandrew.bookkeeper.spendings.impl

import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spendings.access.SpendingStorage
import com.marsofandrew.bookkeeper.spendings.fixtures.spending
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingAddingImplTest {

    private val spendingStorage = mockk<SpendingStorage>()

    private lateinit var addingSpendingImpl: SpendingAddingImpl

    @BeforeEach
    fun setup() {
        addingSpendingImpl = SpendingAddingImpl(spendingStorage)
    }

    @Test
    fun `add adds spending when spending is provided `() {
        val spending = spending(
            id = StringId.unidentified(),
            userId = 5.asId(),
        )

        every { spendingStorage.create(spending) } returns spending

        addingSpendingImpl.add(spending)

        verify(exactly = 1) { spendingStorage.create(spending) }
    }
}