package ai.authservice.controller;



import ai.bffservice.dto.JwtResponseDto;
import com.example.chat.auth.AuthServiceGrpc;
import com.example.chat.auth.LoginRequest;
import com.example.chat.auth.LoginResponse;
import com.example.chat.user.CreateUserRequest;
import com.example.chat.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ai.bffservice.dto.SignupRequestDto;
import ai.bffservice.dto.LoginRequestDto;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub authStub;

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;






    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequestDto request) {

        CreateUserRequest gRpcRequest = CreateUserRequest.newBuilder().build().newBuilder()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword())
                .build();

        userStub.createUser(gRpcRequest);
        return ResponseEntity.status(201).body("Användare skapad framgångsrikt!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto request) {

        LoginRequest gRpcRequest = LoginRequest.newBuilder()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword())
                .build();

        LoginResponse gRpcResponse = authStub.login(gRpcRequest);
        return ResponseEntity.ok(new JwtResponseDto(gRpcResponse.getToken()));
    }
}