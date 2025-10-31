package com.microservice.event.consumer;
import com.microservice.event.dto.StatsMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageConsumer {

    @KafkaListener(
            topics = "sports.events",
            groupId = "event-service-group",
            containerFactory = "publishedMessageKafkaListenerFactory"
    )

    public void consume(StatsMessageDTO message) {
        log.info("Consumed from Kafka: {}", message);
    }
}
