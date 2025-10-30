package com.microservice.event.service;
import com.microservice.event.dto.EventStatusRequest.Status;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import static org.assertj.core.api.Assertions.assertThat;

class EventRegistryServiceTest {
    @Test void shouldAddAndRemoveLiveEvent() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        SchedulerService mockScheduler = Mockito.mock(SchedulerService.class);
        EventRegistryService service = new EventRegistryService(scheduler, mockScheduler);
        service.updateStatus("e1", Status.LIVE);
        assertThat(service.getLiveEvents()).contains("e1");
        service.updateStatus("e1", Status.NOT_LIVE);
        assertThat(service.getLiveEvents()).doesNotContain("e1");
    }
}
