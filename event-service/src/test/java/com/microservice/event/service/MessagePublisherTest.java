package com.microservice.event.service;

import com.microservice.event.dto.StatsMessageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessagePublisherTest {

    @Mock
    private KafkaTemplate<String, StatsMessageDTO> kafkaTemplate;

    @Test
    void shouldPublishMessage() {
        // Arrange
        MessagePublisher publisher = new MessagePublisher(kafkaTemplate);

        // Act
        publisher.publish(new StatsMessageDTO("e1", "1:0", java.time.Instant.now()));

        // Assert
        verify(kafkaTemplate).send(eq("sports.events"), eq("e1"), any());
    }
}
