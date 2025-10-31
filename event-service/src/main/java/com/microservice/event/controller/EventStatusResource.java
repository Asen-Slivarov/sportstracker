package com.microservice.event.controller;

import com.microservice.event.dto.EventStatusRequest;
import com.microservice.event.service.EventRegistryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Event Management",
        description = "Endpoints for managing the lifecycle and status of events."
)
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventStatusResource {

    private final EventRegistryService eventRegistryService;

    @Operation(
            summary = "Update event status",
            description = """
            Updates the status of an event (e.g., LIVE, NOT_LIVE).
            This endpoint is typically called when an event starts or ends.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The event ID and new status to be updated.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = EventStatusRequest.class),
                            examples = @ExampleObject(
                                    name = "Sample Request",
                                    value = """
                        {
                          "eventId": "match123",
                          "status": "LIVE"
                        }
                        """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event status updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or status"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/status")
    public ResponseEntity<Void> updateStatus(@Valid @RequestBody EventStatusRequest request) {
        eventRegistryService.updateStatus(request.getEventId(), request.getStatus());
        return ResponseEntity.ok().build();
    }
}
