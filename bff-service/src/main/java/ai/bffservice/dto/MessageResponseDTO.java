package ai.bffservice.dto;

public record MessageResponseDTO(String messageId,
                                 String senderId,
                                 String content,
                                 String createdAt,
                                 String updatedAt,
                                 String replyToMessageId,
                                 boolean isDeleted) {
}
