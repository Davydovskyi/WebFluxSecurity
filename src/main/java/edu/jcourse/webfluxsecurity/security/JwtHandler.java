package edu.jcourse.webfluxsecurity.security;

import edu.jcourse.webfluxsecurity.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
public class JwtHandler {

    private final String secret;

    public Mono<VerificationResult> checkToken(String token) {
        return Mono.just(verify(token))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    private VerificationResult verify(String token) {
        Claims claims = getClaims(token);
        Date expiration = claims.getExpiration();

        if (expiration.before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        return new VerificationResult(claims, token);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @AllArgsConstructor
    @Getter
    public static class VerificationResult {
        private Claims claims;
        private String token;
    }
}