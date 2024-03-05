package com.marsofandrew.bookkeeper.event

interface UserEvent : Event {
    val userId: Long
}