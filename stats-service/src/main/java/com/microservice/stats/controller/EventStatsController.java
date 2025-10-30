package com.microservice.stats.controller;
import com.microservice.stats.dto.ExternalApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Random;
@RestController
public class EventStatsController {
    private final Random random = new Random();
    @GetMapping("/stats/event")
    public ExternalApiResponse getStats(@RequestParam String id) {
        return new ExternalApiResponse(id, random.nextInt(5)+":"+random.nextInt(5));
    }
}
