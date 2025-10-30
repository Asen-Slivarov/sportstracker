package com.microservice.event.service;

import com.microservice.event.dto.PublishedMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    @Test
    void shouldConsumeMessage() {
        // Arrange
        MessageConsumer consumer = new MessageConsumer();
        PublishedMessage message = new PublishedMessage("e1", "2:1", Instant.now());

        // Act
        consumer.consume(message);

        // No assertion needed: we’re verifying no exception is thrown
        // In a real case, you could use a log appender or spy to verify logging
    }
}
