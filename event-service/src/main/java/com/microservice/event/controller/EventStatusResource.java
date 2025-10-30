package com.microservice.event.controller;
import com.microservice.event.dto.EventStatusRequest;
import com.microservice.event.service.EventRegistryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventStatusResource {

    private final EventRegistryService eventRegistryService;

    @PostMapping("/status")
    public ResponseEntity<Void> updateStatus(@Valid @RequestBody EventStatusRequest request) {
        eventRegistryService.updateStatus(request.getEventId(), request.getStatus());
        return ResponseEntity.ok().build();
    }
}
