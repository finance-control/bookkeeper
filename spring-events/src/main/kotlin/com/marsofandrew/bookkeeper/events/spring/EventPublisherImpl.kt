package com.marsofandrew.bookkeeper.events.spring

import com.marsofandrew.bookkeeper.events.event.UserEvent
import com.marsofandrew.bookkeeper.events.publisher.EventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
internal class EventPublisherImpl(
    private val applicationEventPublisher: ApplicationEventPublisher
) : EventPublisher {

    override fun publish(event: UserEvent) {
        applicationEventPublisher.publishEvent(event)
    }
}