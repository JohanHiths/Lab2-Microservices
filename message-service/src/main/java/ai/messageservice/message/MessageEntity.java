package ai.messageservice.message;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Getter
@Setter
public class MessageEntity {

    @Getter
    @Setter
    @Id
    private String id;

    private String senderId;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String replyToMessageId;

    private boolean isDeleted = false;


    public MessageEntity() {}

    public MessageEntity(String senderId, String content, String replyToMessageId) {
        this.senderId = senderId;
        this.content = content;
        this.replyToMessageId = replyToMessageId;
        this.isDeleted = false;
    }





}
