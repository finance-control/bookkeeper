package com.marsofandrew.bookkeeper.spending.exception

import com.marsofandrew.bookkeeper.properties.exception.ValidationException
import java.time.LocalDate

class InvalidDateIntervalException(
    val startDate: LocalDate,
    val endDate: LocalDate
) : ValidationException("Start date: $startDate is after $endDate")