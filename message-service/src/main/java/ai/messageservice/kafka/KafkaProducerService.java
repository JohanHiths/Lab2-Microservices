package ai.messageservice.kafka;

import ai.messageservice.message.MessageEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);



    private static final String TOPIC = "chat-messages";


    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }

    public void publishMessageEvent(MessageEntity entity) {
        try {

            String jsonMessage = objectMapper.writeValueAsString(entity);


            kafkaTemplate.send(TOPIC, entity.getId(), jsonMessage);

            logger.info("Message published!");

        } catch (Exception e) {
            logger.error("Error publishing message: ❌ {}", e.getMessage());
        }
    }
}