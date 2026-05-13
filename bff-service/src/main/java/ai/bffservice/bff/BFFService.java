package ai.bffservice.bff;


import com.example.chat.user.CreateUserRequest;
import com.example.chat.user.UserResponse;
import com.example.chat.user.UserServiceGrpc;

import org.springframework.stereotype.Service;

@Service
public class BFFService {


    private UserServiceGrpc.UserServiceBlockingStub userBlockingStub;


    public UserResponse createUser(String username, String password, String displayName) {

        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .setDisplayName(displayName)
                .build();

        return userBlockingStub.createUser(request);
    }


}
