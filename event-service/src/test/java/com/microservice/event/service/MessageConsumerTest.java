package com.microservice.event.service;
import com.microservice.event.dto.PublishedMessage;
import org.junit.jupiter.api.Test;

class MessageConsumerTest {
    @Test void shouldConsumeMessage() {
        MessageConsumer consumer = new MessageConsumer();
        consumer.consume(new PublishedMessage("e1","2:1", java.time.Instant.now()));
        // No assertion; verifying no exception
    }
}
