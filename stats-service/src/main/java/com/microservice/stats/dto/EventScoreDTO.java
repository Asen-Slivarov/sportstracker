package com.microservice.stats.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventScoreDTO {

    private String eventId;
    private String score;

}
