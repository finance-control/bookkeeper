package com.marsofandrew.bookkeeper.event.publisher

import com.marsofandrew.bookkeeper.event.Event

interface EventPublisher {

    fun publish(event: Event)

    fun publish(events: Collection<Event>): Unit = events.forEach(this::publish)
}