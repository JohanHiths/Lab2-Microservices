package ai.botservice.service;

import ai.botservice.dto.*;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);


    private final RestClient restClient;

    @Value("${openrouter.model}")
    private String model;

    private final Cache<String, List<OpenRouterRequestDTO.Message>> chatHistoryStorage = Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    public ChatService(RestClient restClient) {
        this.restClient = restClient;
    }


    public String chatWithLLM(ChatRequestDTO dto) {

        List<OpenRouterRequestDTO.Message> history = chatHistoryStorage.get(
                dto.chatId(),
                k -> Collections.synchronizedList(new ArrayList<>())
        );


        List<OpenRouterRequestDTO.Message> apiMessages = new ArrayList<>();


        apiMessages.add(new OpenRouterRequestDTO.Message("system", dto.personality().getSystemPrompt()));


        synchronized (history) {
            apiMessages.addAll(history);
        }


        apiMessages.add(new OpenRouterRequestDTO.Message("user", dto.message()));

        try {

            String aiContentResponse = fetchResponseFromLLM(apiMessages);


            synchronized (history) {
                history.add(new OpenRouterRequestDTO.Message("user", dto.message()));
                history.add(new OpenRouterRequestDTO.Message("assistant", aiContentResponse));
            }

            return aiContentResponse;

        } catch (Exception e) {
            logger.error("Misslyckades att hämta svar från LLM: {}", e.getMessage());
            throw e;
        }
    }


    public String fetchResponseFromLLM(List<OpenRouterRequestDTO.Message> apiMessages) {

        var openRouterRequest = new OpenRouterRequestDTO(model, apiMessages);


        OpenRouterResponseDTO response = restClient.post()
                .uri("/chat/completions")
                .body(openRouterRequest)
                .retrieve()
                .body(OpenRouterResponseDTO.class);


        if (response == null || response.choices() == null || response.choices().isEmpty()) {

            throw new RuntimeException("Invalid response from OpenRouter: " + response + "");
        }


        return response.choices().getFirst().message().content();
    }


    public void clearChatHistory(String chatId) {
        chatHistoryStorage.invalidate(chatId);
    }
}