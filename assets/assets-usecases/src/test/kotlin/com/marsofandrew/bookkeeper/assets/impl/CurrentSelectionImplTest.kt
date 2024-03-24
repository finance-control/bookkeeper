package com.marsofandrew.bookkeeper.assets.impl

import com.marsofandrew.bookkeeper.properties.Currency
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CurrentSelectionImplTest {


    private lateinit var currencySelectionImpl: CurrencySelectionImpl

    @BeforeEach
    fun setup() {
        currencySelectionImpl = CurrencySelectionImpl()
    }

    @Test
    fun `select returns all Currencies`() {
        val result = currencySelectionImpl.select()

        result shouldContainExactlyInAnyOrder Currency.entries
    }
}