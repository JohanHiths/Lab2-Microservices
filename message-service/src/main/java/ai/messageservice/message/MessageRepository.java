package ai.messageservice.message;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<MessageEntity, String> {
    List<MessageEntity> findAllByOrderByCreatedAtAsc();
}
