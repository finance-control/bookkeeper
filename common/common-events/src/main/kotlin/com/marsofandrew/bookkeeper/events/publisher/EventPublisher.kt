package com.marsofandrew.bookkeeper.events.publisher

import com.marsofandrew.bookkeeper.events.event.Event
import com.marsofandrew.bookkeeper.events.event.UserEvent

interface EventPublisher {

    fun publish(event: Event)

    fun publish(events: Collection<Event>): Unit = events.forEach(this::publish)
}