package com.marsofandrew.bookkeeper.base

import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

fun Clock.date(): LocalDate = LocalDate.ofInstant(instant(), ZoneId.of("Z"))
