package com.microservice.stats.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnStatsForEvent() throws Exception {
        mockMvc.perform(get("/stats/event")
                        .param("id", "match-123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is("match-123")))
                .andExpect(jsonPath("$.score", notNullValue()));
    }
}
