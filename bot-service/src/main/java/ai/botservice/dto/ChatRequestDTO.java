package ai.botservice.dto;

import ai.botservice.personality.Personality;

public record ChatRequestDTO(String chatId, String message, Personality personality) {}
