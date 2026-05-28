package ai.botservice.kafka;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ai.botservice.dto.MessageDTO;

@Service
public class BotKafkaConsumer {


    private final ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(BotKafkaConsumer.class);

    @KafkaListener(topics = "chat-messages", groupId = "bot_service_group_debug_2026_05_28")
    public void consumeChatMessage(String jsonMessage) {
        try {

            var jsonNode = objectMapper.readTree(jsonMessage);

            String senderId = jsonNode.get("senderId").asText();
            String content = jsonNode.get("content").asText();
            String messageId = jsonNode.get("id") != null ? jsonNode.get("id").asText() : "okänt";

            logger.info("\"\uD83E\uDD16 Bot Service \uD83E\uDD16 Snappade upp ett meddelande på Event Bus!\"");
            logger.info("\"   Från användare: \" + senderId");
            logger.info("   Innehåll: \"" + content + "\"");

            if (content.trim().startsWith("!bot")) {
                System.out.println("🎯🤖 Bot Service MEDDELANDET VAR ETT BOT-KOMMANDO!🤖 Dags att göra något...");
                executeBotLogic(content, senderId);
            }

        } catch (Exception e) {
            System.err.println("❌🤖 Bot Service Kunde inte avkoda Kafka-meddelande: " + e.getMessage());
        }
    }

    private void executeBotLogic(String command, String senderId) {
        System.out.println("🤖 Boten svarar på kommandot: " + command);
    }

}