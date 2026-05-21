package ai.userservice.user;


import com.example.chat.user.CreateUserRequest;
import com.example.chat.user.UserResponse;
import com.example.chat.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserGrpcClient userGrpcClient;

    public UserController(UserGrpcClient userGrpcClient) {
        this.userGrpcClient = userGrpcClient;
    }

    @PostMapping

    public String create(@RequestBody UserDTO dto) {


        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setUsername(dto.username())
                .setDisplayName(dto.displayName())
                .setPassword(dto.password())
                .setEmail(dto.email())
                .build();

        UserResponse response = userGrpcClient.createUser(request);

        return "Användare skapad med ID: " + response.getUserId();
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        try {
            UserResponse response = userGrpcClient.getUser(username);

            return ResponseEntity.ok(Map.of(
                    "username", response.getUsername(),
                    "displayName", response.getDisplayName()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Användaren hittades inte");
        }
    }


}

