package com.microservice.stats.controller;

import com.microservice.stats.dto.EventScoreDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Tag(
        name = "Event Statistics",
        description = "Endpoints for retrieving event scores and related statistics."
)
@RestController
public class EventStatsResource {

    private final Random random = new Random();

    @Operation(
            summary = "Get event statistics by event ID",
            description = """
                    Fetches the latest score for a given event by its ID.
                    The response is randomly generated for demo purposes.
                    """,
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Unique identifier of the event",
                            example = "match123",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved event score",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventScoreDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid event ID provided",
                            content = @Content
                    )
            }
    )
    @GetMapping("/stats/event")
    public EventScoreDTO getStats(@RequestParam String id) {
        return new EventScoreDTO(
                id,
                random.nextInt(5) + ":" + random.nextInt(5));
    }
}
