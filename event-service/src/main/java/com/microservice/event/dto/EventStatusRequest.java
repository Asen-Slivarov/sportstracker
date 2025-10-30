package com.microservice.event.dto;
import com.microservice.event.enums.EventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventStatusRequest {

    @NotBlank
    private String eventId;
    @NotNull
    private EventStatus status;

}
