package ai.botservice.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class OpenRouterService {


    @Value("${openrouter.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://openrouter.ai/api/v1")
            .build();

    public String getBotResponse(String userMessage, String personality) {

        String systemPrompt = "You are a helpful assistant acting as a " + personality;


        Map<String, Object> requestBody = Map.of(
                "model", "openai/gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                )
        );

        // Gör anropet
        return restClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("HTTP-Referer", "http://localhost:8089")
                .header("X-Title", "Java Chat Lab")
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }

}
