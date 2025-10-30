package com.microservice.event.service;
import com.microservice.event.dto.PublishedMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

class MessagePublisherTest {
    @Test void shouldPublishMessage() {
        KafkaTemplate<String, PublishedMessage> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        MessagePublisher publisher = new MessagePublisher(kafkaTemplate);
        publisher.publish(new PublishedMessage("e1","1:0", java.time.Instant.now()));
        Mockito.verify(kafkaTemplate).send(Mockito.eq("sports.events"), Mockito.eq("e1"), Mockito.any());
    }
}
