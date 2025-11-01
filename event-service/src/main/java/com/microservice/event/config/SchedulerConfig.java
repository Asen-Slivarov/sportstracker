package com.microservice.event.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;

@Configuration
public class SchedulerConfig {

    @Bean(destroyMethod = "close")
    public ExecutorService vtExecutor() {
        return Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("event-vt-", 0).factory()
        );
    }

    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService tickScheduler() {
        return Executors.newScheduledThreadPool(
                Math.max(8, Runtime.getRuntime().availableProcessors() / 2),
                Thread.ofPlatform().name("tick-", 0).factory()
        );
    }

    @Bean
    public Semaphore concurrentPollCap() {
        return new Semaphore(1_000);
    }

}
