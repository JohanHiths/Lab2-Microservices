package ai.authservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Inaktivera CSRF (krävs för att kunna köra POST från Postman/Externa klienter)
                .csrf(csrf -> csrf.disable())

                // 2. Hantera behörigheter
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").permitAll() // Öppna för registrering
                        .requestMatchers("/api/auth/**").permitAll()  // Öppna för inloggning
                        .anyRequest().authenticated()                 // Resten kräver JWT/Inloggning
                )

                // 3. Gör appen statslös (eftersom vi kör JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}
