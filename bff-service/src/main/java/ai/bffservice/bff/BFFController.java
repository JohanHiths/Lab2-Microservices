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
}