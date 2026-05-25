package ai.botservice.kafka;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BotKafkaConsumer {


    private final ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "chat-messages", groupId = "bot-service-group")
    public void consumeChatMessage(String jsonMessage) {
        try {


            var jsonNode = objectMapper.readTree(jsonMessage);

            String senderId = jsonNode.get("senderId").asText();
            String content = jsonNode.get("content").asText();
            String messageId = jsonNode.get("id") != null ? jsonNode.get("id").asText() : "okänt";

            System.out.println("🤖 Bot Service 🤖 Snappade upp ett meddelande på Event Bus!");
            System.out.println("   Från användare: " + senderId);
            System.out.println("   Innehåll: \"" + content + "\"");


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