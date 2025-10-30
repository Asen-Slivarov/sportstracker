package com.microservice.event.service;

import com.microservice.event.dto.EventStatusRequest.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EventRegistryServiceTest {

    @Mock
    private SchedulerService schedulerService; // mocked dependency

    @Test
    void shouldAddAndRemoveLiveEvent() {
        // Arrange
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();

        EventRegistryService service = new EventRegistryService(scheduler, schedulerService);

        // Act + Assert
        service.updateStatus("e1", Status.LIVE);
        assertThat(service.getLiveEvents()).contains("e1");

        service.updateStatus("e1", Status.NOT_LIVE);
        assertThat(service.getLiveEvents()).doesNotContain("e1");
    }
}
