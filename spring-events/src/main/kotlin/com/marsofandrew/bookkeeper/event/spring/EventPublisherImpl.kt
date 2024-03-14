package com.marsofandrew.bookkeeper.event.spring

import com.marsofandrew.bookkeeper.event.Event
import com.marsofandrew.bookkeeper.event.publisher.EventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
internal class EventPublisherImpl(
    private val applicationEventPublisher: ApplicationEventPublisher
) : EventPublisher {

    override fun publish(event: Event) {
        applicationEventPublisher.publishEvent(event)
    }
}
