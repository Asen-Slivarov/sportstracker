package com.microservice.event.controller;

import com.client.statsclient.StatsServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.event.enums.EventStatus;
import com.microservice.event.dto.EventStatusRequest;
import com.microservice.event.service.EventRegistryService;
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
}
