package com.marsofandrew.bookkeeper.events.event

interface UserEvent : Event {
    val userId: Long
}