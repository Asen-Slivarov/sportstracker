package com.microservice.event.service;
import com.microservice.event.dto.StatsMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessagePublisher {

    private final KafkaTemplate<String, StatsMessageDTO> kafkaTemplate;

    public MessagePublisher(KafkaTemplate<String, StatsMessageDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void publish(StatsMessageDTO msg) {
        kafkaTemplate.send("sports.events", msg.getEventId(), msg);
        log.info("Published message {}", msg);
    }

    @Recover
    public void recover(Exception e, StatsMessageDTO msg) {
        log.error("Failed to publish {}", msg, e);
    }
}
