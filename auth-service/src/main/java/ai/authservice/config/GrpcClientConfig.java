package ai.authservice.config;

import com.example.chat.user.UserServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceStub(GrpcChannelFactory channelFactory) {

        return UserServiceGrpc.newBlockingStub(channelFactory.createChannel("user-service"));
    }
}
