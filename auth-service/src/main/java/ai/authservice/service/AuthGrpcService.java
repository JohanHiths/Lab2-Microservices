package ai.authservice.service;


import ai.authservice.jwt.JwtTokenProvider;
import com.example.chat.auth.AuthServiceGrpc;
import com.example.chat.auth.LoginRequest;
import com.example.chat.auth.LoginResponse;
import com.example.chat.user.UserResponse;
import com.example.chat.user.UserServiceGrpc;
import com.example.chat.user.UsernameRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@GrpcService
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        try {
            // 1. Ring User Service för att hämta användaren baserat på användarnamn
            UsernameRequest userReq = UsernameRequest.newBuilder()
                    .setUsername(request.getUsername())
                    .build();

            UserResponse user = userStub.getUserByUsername(userReq);


            boolean matches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

            if (!matches) {
                throw new RuntimeException("Fel lösenord, kompis!");
            }


            String token = tokenProvider.generateToken(user.getUserId(), user.getUsername());


            LoginResponse response = LoginResponse.newBuilder()
                    .setToken(token)
                    .setMessage("Inloggning lyckades!")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {

            responseObserver.onError(Status.UNAUTHENTICATED
                    .withDescription("Fel användarnamn eller lösenord")
                    .asRuntimeException());
        }
    }
}
