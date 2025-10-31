package com.client.statsclient;
import com.client.statsclient.dto.StatsApiResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class StatsServiceClient {

    private final WebClient webClient;

    public StatsServiceClient(@Value("${stats.service.url:http://localhost:8082}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public StatsApiResponseDTO fetchStats(String eventId) {
        return webClient.get()
                .uri("/stats/event?id=" + eventId)
                .retrieve()
                .bodyToMono(StatsApiResponseDTO.class)
                .block();
    }
}
