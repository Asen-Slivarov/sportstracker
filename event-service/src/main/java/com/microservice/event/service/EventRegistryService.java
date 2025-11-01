package com.microservice.event.service;
import com.microservice.event.enums.EventStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EventRegistryService {

    private final ScheduledExecutorService tickScheduler;
    private final ExecutorService vtExecutor;
    private final SchedulerService schedulerService;
    private final Semaphore concurrentPollCap;

    private final ConcurrentHashMap<String, ScheduledFuture<?>> jobs = new ConcurrentHashMap<>();

    public void updateStatus(String eventId, EventStatus status) {
        if (status == EventStatus.LIVE && !jobs.containsKey(eventId)) {
            long jitterMs = ThreadLocalRandom.current().nextLong(0, 10_000);

            ScheduledFuture<?> f = tickScheduler.scheduleAtFixedRate(
                    () -> {
                        if (!concurrentPollCap.tryAcquire()) {
                            return;
                        }
                        vtExecutor.submit(() -> {
                            try {
                                schedulerService.pollAndPublish(eventId);
                            } finally {
                                concurrentPollCap.release();
                            }
                        });
                    },
                    jitterMs,
                    10_000L,
                    TimeUnit.MILLISECONDS
            );

            jobs.put(eventId, f);

        } else if (status == EventStatus.NOT_LIVE) {
            ScheduledFuture<?> f = jobs.remove(eventId);
            if (f != null) f.cancel(false);
        }
    }

    public Set<String> getLiveEvents() { return jobs.keySet(); }

}
