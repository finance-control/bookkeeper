package com.marsofandrew.bookkeeper.properties.util

import java.math.BigDecimal

internal fun BigDecimal.normalize(): BigDecimal {
    var normalized = this
    while (normalized.scale() > 0 && (normalized.movePointRight(normalized.scale()).longValueExact() % 10 == 0L)) {
        normalized = BigDecimal(
            normalized.movePointRight(normalized.scale()).longValueExact() / 10
        ).movePointLeft(normalized.scale() - 1)
    }

    return normalized
}
