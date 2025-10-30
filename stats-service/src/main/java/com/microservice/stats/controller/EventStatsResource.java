package com.microservice.stats.controller;
import com.microservice.stats.dto.EventScoreDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Random;

@RestController
public class EventStatsResource {

    private final Random random = new Random();

    @GetMapping("/stats/event")
    public EventScoreDTO getStats(@RequestParam String id) {
        return new EventScoreDTO(id, random.nextInt(5)+":"+random.nextInt(5));
    }

}
