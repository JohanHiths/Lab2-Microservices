package ai.bffservice.auth;

import ai.bffservice.dto.LoginRequestDto;
import com.example.chat.auth.AuthServiceGrpc;
import com.example.chat.auth.LoginRequest;
import com.example.chat.auth.LoginResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub authStub;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        LoginRequest gRpcRequest = LoginRequest.newBuilder()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword())
                .build();

        LoginResponse response = authStub.login(gRpcRequest);
        return ResponseEntity.ok(response.getToken());
    }

}