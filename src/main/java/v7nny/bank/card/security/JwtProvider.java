package v7nny.bank.card.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.lifetime}") private Duration jwtLifetime;
    private final SecretKey SECRET_KEY;

    public JwtProvider(@Value("${jwt.secret}") String secretString) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public String generateToken(Authentication authentication) {
        var now = new Date();
        var expiration = new Date(now.getTime() + jwtLifetime.toMillis());

        return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
