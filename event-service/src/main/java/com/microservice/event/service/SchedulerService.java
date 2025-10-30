package com.microservice.event.service;

import com.client.statsclient.dto.ExternalApiResponse;
import com.microservice.event.dto.StatsMessageDTO;
import com.microservice.event.mapper.MessageMapper;
import com.client.statsclient.StatsServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final StatsServiceClient statsClient;
    private final MessagePublisher publisher;
    private final MessageMapper mapper;

    public void pollAndPublish(String eventId) {
        try {
            ExternalApiResponse response = statsClient.fetchStats(eventId);
            StatsMessageDTO msg = mapper.toMessage(response);
            publisher.publish(msg);
            log.info("Published {}", msg);
        } catch (Exception e) {
            log.error("Error polling stats for {}", eventId, e);
        }
    }
}
