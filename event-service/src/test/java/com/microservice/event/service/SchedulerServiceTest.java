package com.microservice.event.service;

import com.microservice.event.dto.StatsMessageDTO;
import com.microservice.event.mapper.MessageMapper;
import com.client.statsclient.StatsServiceClient;
import com.client.statsclient.dto.ExternalApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
                .thenReturn(new StatsMessageDTO("e1", "1:0", Instant.now()));

        SchedulerService service = new SchedulerService(client, publisher, mapper);

        // Act
        service.pollAndPublish("e1");

        // Assert
        verify(publisher).publish(any(StatsMessageDTO.class));
    }

    @Test
    void shouldHandleExceptionFromStatsClient() {
        when(client.fetchStats("e1")).thenThrow(new RuntimeException("Stats API down"));

        SchedulerService service = new SchedulerService(client, publisher, mapper);

        service.pollAndPublish("e1");

        verify(publisher, never()).publish(any());
    }

    @Test
    void shouldHandleExceptionFromMapper() {
        when(client.fetchStats("e1")).thenReturn(new ExternalApiResponse("e1", "1:0"));
        when(mapper.toMessage(any())).thenThrow(new RuntimeException("Mapping failed"));

        SchedulerService service = new SchedulerService(client, publisher, mapper);

        service.pollAndPublish("e1");

        verify(publisher, never()).publish(any());
    }

    @Test
    void shouldHandleExceptionFromPublisher() {
        when(client.fetchStats("e1")).thenReturn(new ExternalApiResponse("e1", "1:0"));
        when(mapper.toMessage(any())).thenReturn(new StatsMessageDTO("e1", "1:0", Instant.now()));
        doThrow(new RuntimeException("Kafka unavailable"))
                .when(publisher).publish(any());

        SchedulerService service = new SchedulerService(client, publisher, mapper);

        service.pollAndPublish("e1");

        verify(publisher).publish(any());
    }

    @Test
    void shouldPollAndPublishMultipleEvents() {
        when(client.fetchStats(anyString()))
                .thenAnswer(inv -> new ExternalApiResponse(inv.getArgument(0), "2:2"));
        when(mapper.toMessage(any())).thenReturn(new StatsMessageDTO("x", "2:2", Instant.now()));

        SchedulerService service = new SchedulerService(client, publisher, mapper);

        service.pollAndPublish("match-1");
        service.pollAndPublish("match-2");

        verify(publisher, times(2)).publish(any());
    }


}
