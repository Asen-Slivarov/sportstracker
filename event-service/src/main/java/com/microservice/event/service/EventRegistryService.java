package com.microservice.event.service;
import com.microservice.event.enums.EventStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class EventRegistryService {

    private final TaskScheduler scheduler;
    private final SchedulerService schedulerService;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> jobs = new ConcurrentHashMap<>();

    public void updateStatus(String eventId, EventStatus status) {

        if (status == EventStatus.LIVE && !jobs.containsKey(eventId)) {
            ScheduledFuture<?> f = scheduler.scheduleAtFixedRate(
                () -> schedulerService.pollAndPublish(eventId), Duration.ofSeconds(10));
            jobs.put(eventId, f);
        } else if (status == EventStatus.NOT_LIVE) {
            ScheduledFuture<?> f = jobs.remove(eventId);
            if (f != null) {
                f.cancel(false);
            }
        }

    }

    public Set<String> getLiveEvents() { return jobs.keySet(); }
}
