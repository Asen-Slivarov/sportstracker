package com.microservice.event.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class EventStatusRequest {
    @NotBlank private String eventId;
    @NotNull private Status status;
    public enum Status { LIVE, NOT_LIVE }
}
