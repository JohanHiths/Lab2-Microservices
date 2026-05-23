package ai.messageservice.message;

import com.example.chat.user.*;
import io.grpc.Server;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class MessageGrpcService extends MessageServiceGrpc.MessageServiceImplBase{


    @Autowired
    private final MessageRepository messageRepository;

    public MessageGrpcService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @Override
    public void sendMessage(SendMessageRequest request, StreamObserver<MessageResponse> responseObserver) {

        MessageEntity entity = new MessageEntity(
                request.getSenderId(),
                request.getContent(),
                request.getReplyToMessageId()
        );

        MessageEntity savedEntity = messageRepository.save(entity);


        MessageResponse response = mapToResponse(savedEntity);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getChatHistory(ChatHistoryRequest request, StreamObserver<ChatHistoryResponse> responseObserver) {

        List<MessageEntity> entities = messageRepository.findAllByOrderByCreatedAtAsc();


        ChatHistoryResponse.Builder responseBuilder = ChatHistoryResponse.newBuilder();

        for (MessageEntity entity : entities) {
            responseBuilder.addMessages(mapToResponse(entity));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteMessage(DeleteMessageRequest request, StreamObserver<DeleteMessageResponse> responseObserver) {

        MessageEntity entity = messageRepository.findById(request.getMessageId())
                .orElseThrow(() -> new RuntimeException("Meddelandet hittades inte!"));

        if (!entity.getSenderId().equals(request.getUserId())) {
            responseObserver.onError(Status.PERMISSION_DENIED
                    .withDescription("Du får inte radera någon annans meddelande!")
                    .asRuntimeException());
            return;
        }

        entity.setDeleted(true);
        entity.setContent("Det här meddelandet har raderats.");


        messageRepository.save(entity);


        DeleteMessageResponse response = DeleteMessageResponse.newBuilder()
                .setSuccess(true)
                .setMessageId(entity.getId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }




    private MessageResponse mapToResponse(MessageEntity entity) {
        return MessageResponse.newBuilder()
                .setMessageId(entity.getId())
                .setSenderId(entity.getSenderId())
                .setContent(entity.getContent())
                .setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : "")
                .setUpdatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : "")
                .setReplyToMessageId(entity.getReplyToMessageId() != null ? entity.getReplyToMessageId() : "")
                .setIsDeleted(entity.isDeleted())
                .build();
    }

}
