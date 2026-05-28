package ai.botservice.dto;

import java.util.List;

import ai.botservice.personality.Personality;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenRouterResponseDTO(List<Choice> choices) {

    public record Choice(Message message) {
        public record Message(String role, String content) {}
    }
}