package ai.userservice.user;

import com.example.chat.user.*;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import net.devh.boot.grpc.server.service.GrpcService;

import org.springframework.security.crypto.password.PasswordEncoder;


@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public UserGrpcService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        String rawPassword = request.getPassword();
        String Password = passwordEncoder.encode(rawPassword);

        user.setUsername(request.getUsername());
        user.setPassword(Password);
        user.setDisplayName(request.getDisplayName());
        user.setEmail(request.getEmail());

        UserEntity savedUser = userRepository.save(user);

        UserResponse response = UserResponse.newBuilder()
                .setUserId(savedUser.getId().toString())
                .setUsername(savedUser.getUsername())
                .setPasswordHash(savedUser.getPassword())
                .setDisplayName(savedUser.getDisplayName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void getUserByUsername(UsernameRequest request,
                                  StreamObserver<UserResponse> responseObserver) {

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = UserResponse.newBuilder()
                .setUserId(user.getId().toString())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setPasswordHash(user.getPassword())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }





}