package com.marsofandrew.bookkeeper.assets

import com.marsofandrew.bookkeeper.assets.impl.CurrencySelectionImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class AssetsContextConfiguration {

    @Bean
    fun currencySelection(): CurrencySelection = CurrencySelectionImpl()
}