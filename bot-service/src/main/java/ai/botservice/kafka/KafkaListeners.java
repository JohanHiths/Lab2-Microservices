package ai.botservice.kafka;

import ai.botservice.dto.MessageDTO;
import ai.botservice.dto.BotResponseDTO;
import ai.botservice.dto.ChatRequestDTO;
import ai.botservice.personality.Personality;
import ai.botservice.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;



@Component
public class KafkaListeners {

    private final ChatService chatService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    Logger logger = LoggerFactory.getLogger(KafkaListeners.class);

    public KafkaListeners(ChatService chatService, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.chatService = chatService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "chat-messages", groupId = "bot_service_group_debug_2026_05_28")

    public void listen(String data) {

        logger.info("Ropar på kafka: {}", data);


        try {

            MessageDTO incoming = objectMapper.readValue(data, MessageDTO.class);

            logger.info("Lyssnare aktiverad! Användaren skrev: {}",  incoming.content());


            Personality personality = Personality.valueOf(incoming.personality());


            ChatRequestDTO chatRequest = new ChatRequestDTO(
                    incoming.chatId(),
                    incoming.content(),
                    personality
            );





            String aiResponseText = chatService.chatWithLLM(chatRequest);


            BotResponseDTO responseDTO = new BotResponseDTO(
                    aiResponseText,
                    incoming.chatId(),
                    incoming.personality()
            );


            kafkaTemplate.send("chat-responses", objectMapper.writeValueAsString(responseDTO));

            logger.info("🚀 AI-svar skickat tillbaka till Kafka!");

        } catch (Exception e) {
            logger.error("\"❌ Fel i Bot-lyssnaren: " + e.getMessage());
            e.printStackTrace();
        }
    }

}