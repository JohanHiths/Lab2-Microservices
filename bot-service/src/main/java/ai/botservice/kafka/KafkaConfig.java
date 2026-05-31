package ai.botservice.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConfig {


    @Bean
    public NewTopic messagesTopic() {
        return TopicBuilder.name("chat-messages")
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic chatResponsesTopic() {
        return TopicBuilder.name("chat-responses")
                .partitions(3)
                .build();
    }



    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            KafkaTemplate<String, String> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);


        DeadLetterPublishingRecoverer dltRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);


        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                dltRecoverer,
                new FixedBackOff(1000L, 2L)
        );

        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }


}
