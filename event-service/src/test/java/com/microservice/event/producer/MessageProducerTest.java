package com.microservice.event.producer;

import com.microservice.event.dto.StatsMessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageProducerTest {

    @Mock
    private KafkaTemplate<String, StatsMessageDTO> kafkaTemplate;

    @InjectMocks
    private MessageProducer producer;

    private StatsMessageDTO message;

    @BeforeEach
    void setup() {
        message = new StatsMessageDTO("match1", "2:1", Instant.now());
        ReflectionTestUtils.setField(producer, "topic", "sports.events");
    }

    @Test
    void shouldPublishMessageSuccessfully() {
        // Arrange
        SendResult<String, StatsMessageDTO> result = mock(SendResult.class);
        CompletableFuture<SendResult<String, StatsMessageDTO>> future =
                CompletableFuture.completedFuture(result);
        when(kafkaTemplate.send(eq("sports.events"), eq("match1"), any())).thenReturn(future);

        // Act
        producer.publish(message);

        // Assert
        verify(kafkaTemplate).send(eq("sports.events"), eq("match1"), eq(message));
    }

    @Test
    void shouldCallRecoverWhenRetriesExhausted() {
        // Arrange
        Exception exception = new Exception("Final failure");

        // Act & Assert
        assertThatCode(() -> producer.recover(exception, message))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldHandleNullResultGracefully() {
        // Arrange
        CompletableFuture<SendResult<String, StatsMessageDTO>> future = new CompletableFuture<>();
        future.complete(null);
        when(kafkaTemplate.send(anyString(), anyString(), any())).thenReturn(future);

        // Act & Assert
        assertThatCode(() -> producer.publish(message)).doesNotThrowAnyException();

        verify(kafkaTemplate).send(anyString(), anyString(), any());
    }
}
