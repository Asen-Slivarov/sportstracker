package com.microservice.event.service;

import com.microservice.event.dto.PublishedMessage;
import com.microservice.event.mapper.MessageMapper;
import com.client.statsclient.StatsServiceClient;
import com.client.statsclient.dto.ExternalApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @Mock
    private StatsServiceClient client;

    @Mock
    private MessagePublisher publisher;

    @Mock
    private MessageMapper mapper;

    @Test
    void shouldPollAndPublish() {
        // Arrange
        when(client.fetchStats("e1"))
                .thenReturn(new ExternalApiResponse("e1", "1:0"));
        when(mapper.toMessage(any()))
                .thenReturn(new PublishedMessage("e1", "1:0", Instant.now()));

        SchedulerService service = new SchedulerService(client, publisher, mapper);

        // Act
        service.pollAndPublish("e1");

        // Assert
        verify(publisher).publish(any(PublishedMessage.class));
    }
}
