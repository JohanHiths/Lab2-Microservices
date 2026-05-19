package ai.bffservice.auth;

import ai.bffservice.dto.LoginRequestDto;
import ai.bffservice.dto.SignupRequestDto;
import com.example.chat.auth.AuthServiceGrpc;
import com.example.chat.auth.LoginRequest;
import com.example.chat.auth.LoginResponse;
import com.example.chat.user.CreateUserRequest;
import com.example.chat.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub authStub;

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            LoginRequest gRpcRequest = LoginRequest.newBuilder()
                    .setUsername(request.getUsername())
                    .setPassword(request.getPassword())
                    .build();

            LoginResponse response = authStub.login(gRpcRequest);
            return ResponseEntity.ok(response.getToken());
        }
         catch
            (io.grpc.StatusRuntimeException e){

                if (e.getStatus().getCode() == io.grpc.Status.Code.UNAUTHENTICATED) {
                    return ResponseEntity.status(401).body("Fel användarnamn eller lösenord (Från gRPC)");
                }

                return ResponseEntity.status(500).body("Internt systemfel: " + e.getStatus().getDescription());

            }
            finally {
            System.out.println("asd");

        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequestDto request) {

        CreateUserRequest gRpcRequest = CreateUserRequest.newBuilder()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword())
                .build();


        userStub.createUser(gRpcRequest);
        return ResponseEntity.status(201).body("Användare skapad framgångsrikt!");
    }

}