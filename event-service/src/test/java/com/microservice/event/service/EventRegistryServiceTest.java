package com.microservice.event.service;

import com.microservice.event.enums.EventStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EventRegistryServiceTest {

    @Mock
    private SchedulerService schedulerService; // Mocked dependency

    @Test
    void shouldAddAndRemoveLiveEvent() {
        // Arrange
        ScheduledExecutorService tickScheduler =
                Executors.newScheduledThreadPool(2, Thread.ofPlatform().name("tick-", 0).factory());

        ExecutorService vtExecutor =
                Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("event-vt-", 0).factory());

        Semaphore cap = new Semaphore(1000);

        EventRegistryService service = new EventRegistryService(tickScheduler, vtExecutor, schedulerService, cap);

        // Act
        service.updateStatus("e1", EventStatus.LIVE);

        // Assert that the event was added
        Set<String> liveEvents = service.getLiveEvents();
        assertThat(liveEvents).containsExactly("e1");

        // Act - deactivate the event
        service.updateStatus("e1", EventStatus.NOT_LIVE);

        // Assert that the event was removed
        assertThat(service.getLiveEvents()).doesNotContain("e1");

        tickScheduler.shutdownNow();
        vtExecutor.close();
    }

}
