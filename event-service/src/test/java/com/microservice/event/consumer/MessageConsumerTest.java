package com.microservice.event.consumer;

import com.microservice.event.dto.StatsMessageDTO;
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
        StatsMessageDTO message = new StatsMessageDTO("e1", "2:1", Instant.now());

        // Act
        consumer.consume(message);

        // No assertion needed: we’re verifying no exception is thrown
        // In a real case, you could use a log appender or spy to verify logging
    }
}
