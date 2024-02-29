package com.marsofandrew.bookkeeper.events.publisher

import com.marsofandrew.bookkeeper.events.event.UserEvent

interface EventPublisher {

    fun publish(event: UserEvent)
}