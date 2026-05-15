package ai.userservice.user;


import com.example.chat.user.CreateUserRequest;
import com.example.chat.user.UserResponse;
import com.example.chat.user.UserServiceGrpc;
import com.example.chat.user.UsernameRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class UserGrpcClient {

    private final UserServiceGrpc.UserServiceBlockingStub userStub;




    public UserGrpcClient(UserServiceGrpc.UserServiceBlockingStub userStub) {
        this.userStub = userStub;
    }

    public UserGrpcClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
        this.userStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public UserResponse createUser(CreateUserRequest request) {
        return userStub.createUser(request);
    }




    public UserResponse getUser(String username) {
        UsernameRequest request = UsernameRequest.newBuilder()
                .setUsername(username)
                .build();

        return userStub.getUserByUsername(request);
    }



}
