package com.marsofandrew.bookkeeper.assets.controller

import com.marsofandrew.bookkeeper.assets.CurrencySelection
import com.marsofandrew.bookkeeper.assets.controller.dto.AssetDto
import com.marsofandrew.bookkeeper.assets.controller.dto.toAssetsDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/assets")
internal class AssetsController(
    private val currencySelection: CurrencySelection
) {

    @GetMapping("/currency")
    fun get(): List<AssetDto> {
        return currencySelection.select().map { it.toAssetsDto() }
    }
}
