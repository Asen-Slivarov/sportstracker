package com.microservice.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.event.dto.EventStatusRequest;
import com.microservice.event.dto.EventStatusRequest.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EventStatusResourceTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void shouldAcceptValidStatus() throws Exception {
        // Arrange
        EventStatusRequest request = new EventStatusRequest();
        request.setEventId("match1");
        request.setStatus(Status.LIVE);

        // Act + Assert
        mockMvc.perform(post("/events/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
