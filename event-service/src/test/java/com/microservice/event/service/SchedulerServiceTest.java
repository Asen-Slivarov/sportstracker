package com.microservice.event.service;
import com.microservice.event.dto.PublishedMessage;
import com.microservice.event.mapper.MessageMapper;
import com.client.statsclient.StatsServiceClient;
import com.client.statsclient.dto.ExternalApiResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

class SchedulerServiceTest {
    @Test void shouldPollAndPublish() {
        StatsServiceClient client = Mockito.mock(StatsServiceClient.class);
        MessagePublisher publisher = Mockito.mock(MessagePublisher.class);
        MessageMapper mapper = Mockito.mock(MessageMapper.class);
        Mockito.when(client.fetchStats("e1")).thenReturn(new ExternalApiResponse("e1","1:0"));
        Mockito.when(mapper.toMessage(any())).thenReturn(new PublishedMessage("e1","1:0", java.time.Instant.now()));
        SchedulerService service = new SchedulerService(client, publisher, mapper);
        service.pollAndPublish("e1");
        Mockito.verify(publisher).publish(any());
    }
}
