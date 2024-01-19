package edu.jcourse.webfluxsecurity.security;

import edu.jcourse.webfluxsecurity.dto.UserDto;
import edu.jcourse.webfluxsecurity.exception.AuthException;
import edu.jcourse.webfluxsecurity.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;

    private TokenDetails generateToken(UserDto user) {
        Map<String, Object> claims = Map.of(
                "role", user.role(),
                "username", user.username()
        );
        return generateToken(claims, user.id().toString());
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims, subject);
    }

    private TokenDetails generateToken(Date expireDate, Map<String, Object> claims, String subject) {
        Date issuedAt = new Date();
        String token = Jwts.builder()
                .claims(claims)
                .issuer(issuer)
                .subject(subject)
                .issuedAt(issuedAt)
                .id(UUID.randomUUID().toString())
                .expiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();
        return TokenDetails.builder()
                .token(token)
                .expireDate(expireDate)
                .issuedAt(issuedAt)
                .build();
    }

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userService.findByUsername(username)
                .flatMap(user -> {
                    if (!user.enabled()) {
                        return Mono.error(new AuthException("USER_DISABLED", "User not enabled"));
                    }

                    if (!passwordEncoder.matches(password, user.password())) {
                        return Mono.error(new AuthException("INVALID_CREDENTIALS", "Invalid credentials"));
                    }
                    TokenDetails tokenDetails = generateToken(user);
                    tokenDetails.setUserId(user.id());
                    return Mono.just(tokenDetails);
                })
                .switchIfEmpty(Mono.error(new AuthException("USER_NOT_FOUND", "User not found")));
    }
}