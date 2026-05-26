package ai.messageservice.kafka;

import ai.messageservice.message.MessageEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private static final String TOPIC = "chat-messages";


    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }

    public void publishMessageEvent(MessageEntity entity) {
        try {

            String jsonMessage = objectMapper.writeValueAsString(entity);

            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(TOPIC, entity.getId(), jsonMessage);

            future.whenComplete((result, ex) -> {
                if (ex == null) {

                    long offset = result.getRecordMetadata().offset();
                    int partition = result.getRecordMetadata().partition();
                    System.out.println("🔥 Kafka Meddelande publicerat! Topic: " + TOPIC +
                            " | Partition: " + partition +
                            " | Offset: " + offset);
                    logger.info("[Kafka] Meddelande publicerat! Topic: \" + TOPIC +\n" +
                            " \" | Partition: \" + partition +\n" +
                            " \" | Offset: \" + offset);");
                } else {

                    logger.error("❌ [Kafka] Kunde inte leverera meddelande till broker: " + ex.getMessage());
                }
            });

        } catch (Exception e) {

            logger.error("❌ Kafka Serialiseringsfel: " + e.getMessage());
        }
    }
}