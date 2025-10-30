package com.microservice.event.service;

import com.microservice.event.dto.PublishedMessage;
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
            var response = statsClient.fetchStats(eventId);
            PublishedMessage msg = mapper.toMessage(response);
            publisher.publish(msg);
            log.info("Published {}", msg);
        } catch (Exception e) {
            log.error("Error polling stats for {}", eventId, e);
        }
    }
}
