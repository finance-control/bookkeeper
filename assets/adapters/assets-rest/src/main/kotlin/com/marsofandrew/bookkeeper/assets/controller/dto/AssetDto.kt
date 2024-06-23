package com.marsofandrew.bookkeeper.assets.controller.dto

import com.marsofandrew.bookkeeper.assets.CurrencyAsset

internal data class AssetDto(
    val code: String,
    val title: String
)

internal fun CurrencyAsset.toAssetsDto() = AssetDto(
    code = currency.name,
    title = title
)
