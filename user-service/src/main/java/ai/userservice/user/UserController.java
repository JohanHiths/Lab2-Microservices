package ai.userservice.user;


import com.example.chat.user.CreateUserRequest;
import com.example.chat.user.UserResponse;
import com.example.chat.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceGrpc.UserServiceBlockingStub userStub;



    @PostMapping
    public String create(@RequestBody UserDTO dto) {


        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setUsername(dto.username())
                .setDisplayName(dto.displayName())
                .setPassword(dto.password())
                .setEmail(dto.email())
                .build();


        UserResponse response = userStub.createUser(request);

        return "Användare skapad med ID: " + response.getUserId();
    }
}

