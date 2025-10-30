package com.microservice.event.service;
import com.microservice.event.dto.PublishedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageConsumer {
    @KafkaListener(topics = "sports.events", groupId = "event-service-group",
                   containerFactory = "publishedMessageKafkaListenerFactory")
    public void consume(PublishedMessage message) {
        log.info("Consumed from Kafka: {}", message);
    }
}
