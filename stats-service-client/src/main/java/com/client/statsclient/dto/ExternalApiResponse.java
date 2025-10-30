package com.client.statsclient.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @NoArgsConstructor @AllArgsConstructor
public class ExternalApiResponse {
    private String eventId;
    private String score;
}
