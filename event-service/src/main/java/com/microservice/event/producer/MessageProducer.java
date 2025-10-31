package com.microservice.event.producer;

import com.microservice.event.dto.StatsMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageProducer {

    private final KafkaTemplate<String, StatsMessageDTO> kafkaTemplate;

    @Value("${kafka.topics.sports-events:sports.events}")
    private String topic;

    public MessageProducer(KafkaTemplate<String, StatsMessageDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void publish(StatsMessageDTO msg) {
        log.info("Attempting to publish message to Kafka topic [{}]: {}", topic, msg);

        kafkaTemplate.send(topic, msg.getEventId(), msg)
                .whenComplete((SendResult<String, StatsMessageDTO> result, Throwable ex) -> {
                    if (ex == null) {
                        log.info("Successfully published event [{}] to Kafka at offset {}",
                                msg.getEventId(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to publish message [{}]: {}", msg.getEventId(), ex.getMessage());
                        throw new RuntimeException(ex);
                    }
                });
    }

    @Recover
    public void recover(Exception e, StatsMessageDTO msg) {
        log.error("Failed to publish after retries for event [{}]. Error: {}", msg.getEventId(), e.getMessage(), e);
    }
}
