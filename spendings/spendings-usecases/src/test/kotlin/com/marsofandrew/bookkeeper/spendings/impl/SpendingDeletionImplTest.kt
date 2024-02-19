package com.marsofandrew.bookkeeper.spendings.impl

import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.access.SpendingStorage
import com.marsofandrew.bookkeeper.spendings.SpendingDeletion
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SpendingDeletionImplTest {

    private val spendingStorage = mockk<SpendingStorage>(relaxUnitFun = true)

    private lateinit var spendingDeletion: SpendingDeletion

    @BeforeEach
    fun setup() {
        spendingDeletion = SpendingDeletionImpl(spendingStorage)
    }

    @Test
    fun `delete deletes spending by ids`() {
        val ids = setOf<StringId<Spending>>(
            StringId("55"),
            StringId("45"),
            StringId("35"),
        )

        spendingDeletion.delete(ids)

        verify(exactly = 1) { spendingDeletion.delete(ids) }
    }
}