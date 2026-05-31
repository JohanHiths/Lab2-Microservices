package ai.botservice.dto;

public record MessageDTO(String chatId,
                         String content,
                         String replyToMessageId,
                         String personality) {
}
