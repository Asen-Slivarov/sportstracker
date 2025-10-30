package com.microservice.event.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
@Data @NoArgsConstructor @AllArgsConstructor
public class PublishedMessage {
    private String eventId;
    private String score;
    private Instant timestamp;
}
