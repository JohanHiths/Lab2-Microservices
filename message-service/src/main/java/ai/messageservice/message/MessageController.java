package ai.messageservice.message;


import com.example.chat.user.MessageResponse;
import com.example.chat.user.MessageServiceGrpc;
import com.example.chat.user.SendMessageRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class MessageController {

    @GrpcClient("message-service")
    private MessageServiceGrpc.MessageServiceBlockingStub messageStub;





}
