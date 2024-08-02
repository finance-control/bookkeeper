package com.marsofandrew.bookkeeper.transfers.impl.utils

import com.marsofandrew.bookkeeper.transfers.exception.InvalidDateIntervalException
import java.time.LocalDate

internal fun validateDates(startDate: LocalDate, endDate: LocalDate) {
    if (startDate > endDate) {
        throw InvalidDateIntervalException(startDate, endDate)
    }
}