package com.microservice.event.controller;

import com.client.statsclient.StatsServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.event.dto.EventStatusRequest;
import com.microservice.event.enums.EventStatus;
import com.microservice.event.service.EventRegistryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventStatusResource.class)
class EventStatusResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventRegistryService eventRegistryService;

    @MockBean
    private StatsServiceClient statsServiceClient;

    @Test
    @DisplayName("Should accept valid status and return 200 OK")
    void shouldAcceptValidStatus() throws Exception {
        EventStatusRequest request = new EventStatusRequest();
        request.setEventId("match1");
        request.setStatus(EventStatus.LIVE);

        doNothing().when(eventRegistryService).updateStatus(any(), any());

        mockMvc.perform(post("/events/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 400 when eventId is missing")
    void shouldRejectWhenEventIdIsMissing() throws Exception {
        EventStatusRequest request = new EventStatusRequest();
        request.setStatus(EventStatus.LIVE);

        mockMvc.perform(post("/events/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when status is missing")
    void shouldRejectWhenStatusIsMissing() throws Exception {
        EventStatusRequest request = new EventStatusRequest();
        request.setEventId("match1");

        mockMvc.perform(post("/events/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when both fields are missing")
    void shouldRejectWhenBothFieldsMissing() throws Exception {
        EventStatusRequest request = new EventStatusRequest();

        mockMvc.perform(post("/events/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when invalid JSON is provided")
    void shouldRejectInvalidJson() throws Exception {
        String invalidJson = "{\"eventId\":, \"status\": \"LIVE\"}"; // malformed JSON

        mockMvc.perform(post("/events/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when invalid enum value is provided")
    void shouldRejectInvalidEnumValue() throws Exception {
        String invalidStatusJson = """
            {
              "eventId": "match1",
              "status": "INVALID_STATUS"
            }
            """;

        mockMvc.perform(post("/events/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidStatusJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 415 when Content-Type header is missing")
    void shouldRejectWithoutContentType() throws Exception {
        EventStatusRequest request = new EventStatusRequest();
        request.setEventId("match1");
        request.setStatus(EventStatus.LIVE);

        mockMvc.perform(post("/events/status")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Should handle extra unknown fields gracefully")
    void shouldIgnoreUnknownFields() throws Exception {
        String jsonWithExtraField = """
            {
              "eventId": "match1",
              "status": "LIVE",
              "extraField": "ignored"
            }
            """;

        doNothing().when(eventRegistryService).updateStatus(any(), any());

        mockMvc.perform(post("/events/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithExtraField))
                .andExpect(status().isOk());
    }
}
