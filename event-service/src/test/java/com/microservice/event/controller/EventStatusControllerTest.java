package com.microservice.event.controller;
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
class EventStatusControllerTest {
    @Autowired MockMvc mockMvc;
    @Test void shouldAcceptValidStatus() throws Exception {
        mockMvc.perform(post("/events/status")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"eventId\":\"match1\",\"status\":\"LIVE\"}"))
            .andExpect(status().isOk());
    }
}
