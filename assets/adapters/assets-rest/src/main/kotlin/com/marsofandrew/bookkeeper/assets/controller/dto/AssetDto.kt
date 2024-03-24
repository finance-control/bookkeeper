package com.marsofandrew.bookkeeper.assets.controller.dto

import com.marsofandrew.bookkeeper.properties.Currency

internal data class AssetDto(
    val code: String,
    val title: String
)

internal fun Currency.toAssetsDto() = AssetDto(
    code = code,
    title = title
)
