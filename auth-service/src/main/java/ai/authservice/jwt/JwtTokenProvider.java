package ai.authservice.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {


    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private final long jwtExpirationInMs = 3600000;

    public String generateToken(String userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);



        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        return Jwts.builder()
                .setSubject(userId)
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
