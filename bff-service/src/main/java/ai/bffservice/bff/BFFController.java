package ai.bffservice.bff;


import ai.bffservice.dto.MessageDTO;
import ai.bffservice.dto.SendMessageDTO;
import com.example.chat.user.MessageResponse;
import com.example.chat.user.MessageServiceGrpc;
import com.example.chat.user.SendMessageRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class BFFController {


    @GrpcClient("message-service")
    private MessageServiceGrpc.MessageServiceBlockingStub messageStub;



    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageDTO dto, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att skicka meddelanden!");
        }


        String senderId = authentication.getName();


        SendMessageRequest gRpcRequest = SendMessageRequest.newBuilder()
                .setSenderId(senderId)
                .setContent(dto.content())
                .setReplyToMessageId(dto.replyToMessageId() != null ? dto.replyToMessageId() : "")
                .build();

        try {

            MessageResponse response = messageStub.sendMessage(gRpcRequest);

            ai.bffservice.dto.MessageResponseDTO responseDto = new ai.bffservice.dto.MessageResponseDTO(
                    response.getMessageId(),
                    response.getSenderId(),
                    response.getContent(),
                    response.getCreatedAt(),
                    response.getUpdatedAt(),
                    response.getReplyToMessageId(),
                    response.getIsDeleted()
            );



            return ResponseEntity.ok(responseDto);

        } catch (io.grpc.StatusRuntimeException e) {
            return ResponseEntity.status(500).body("Kunde inte skicka meddelande via gRPC: " + e.getStatus().getDescription());
        }
    }
    @GetMapping
    public ResponseEntity<?> getChatHistory(Authentication authentication) {


        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att se chatthistorik!");
        }

        com.example.chat.user.ChatHistoryRequest gRpcRequest =
                com.example.chat.user.ChatHistoryRequest.newBuilder().build();

        try {

            com.example.chat.user.ChatHistoryResponse grpcResponse = messageStub.getChatHistory(gRpcRequest);

            java.util.List<ai.bffservice.dto.MessageResponseDTO> historyList = grpcResponse.getMessagesList().stream()
                    .map(msg -> new ai.bffservice.dto.MessageResponseDTO(
                            msg.getMessageId(),
                            msg.getSenderId(),
                            msg.getContent(),
                            msg.getCreatedAt(),
                            msg.getUpdatedAt(),
                            msg.getReplyToMessageId(),
                            msg.getIsDeleted()
                    ))
                    .toList();

            return ResponseEntity.ok(historyList);

        } catch (io.grpc.StatusRuntimeException e) {
            return ResponseEntity.status(500).body("Kunde inte hämta historik via gRPC: " + e.getStatus().getDescription());
        }
    }
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable String messageId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Du måste vara inlogga för att radera meddelanden!");
        }

        String currentUserId = authentication.getName();


        com.example.chat.user.DeleteMessageRequest gRpcRequest = com.example.chat.user.DeleteMessageRequest.newBuilder()
                .setMessageId(messageId)
                .setUserId(currentUserId)
                .build();

        try {
            com.example.chat.user.DeleteMessageResponse grpcResponse = messageStub.deleteMessage(gRpcRequest);
            return ResponseEntity.ok(java.util.Map.of(
                    "success", grpcResponse.getSuccess(),
                    "messageId", grpcResponse.getMessageId(),
                    "status", "Meddelandet har raderats (Soft Delete)"
            ));

        } catch (io.grpc.StatusRuntimeException e) {
            return ResponseEntity.status(500).body("Kunde inte radera meddelande via gRPC: " + e.getStatus().getDescription());
        }
    }

}