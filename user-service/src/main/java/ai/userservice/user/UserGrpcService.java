package ai.userservice.user;

import com.example.chat.user.CreateUserRequest;
import com.example.chat.user.UserRequest;
import com.example.chat.user.UserResponse;
import com.example.chat.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.security.crypto.password.PasswordEncoder;


@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserGrpcService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void getUserProfile(UserRequest request, StreamObserver<UserResponse> responseObserver) {

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        UserResponse response = UserResponse.newBuilder()
                .setUserId(user.getId().toString())
                .setUsername(user.getUsername())
                .setDisplayName(user.getDisplayName())
                .setEmail(user.getEmail())
                .build();


        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public UserResponse mapToResponse(UserEntity entity) {
        return UserResponse.newBuilder()
                .setUserId(entity.getId().toString())
                .setUsername(entity.getUsername())
                .setDisplayName(entity.getDisplayName())
                .setEmail(entity.getEmail())
                .build();
    }
    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {


        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setDisplayName(request.getDisplayName());
        user.setEmail(request.getEmail());


        UserEntity savedUser = userRepository.save(user);


        UserResponse response = UserResponse.newBuilder()
                .setUserId(savedUser.getId().toString())
                .setUsername(savedUser.getUsername())
                .setDisplayName(savedUser.getDisplayName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserByUsername(com.example.chat.user.UsernameRequest request,
                                  StreamObserver<UserResponse> responseObserver) {

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        responseObserver.onNext(mapToResponse(user));
        responseObserver.onCompleted();
    }





}