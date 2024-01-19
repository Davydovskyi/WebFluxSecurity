package edu.jcourse.webfluxsecurity.security;

import io.jsonwebtoken.Claims;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

import static edu.jcourse.webfluxsecurity.security.JwtHandler.VerificationResult;

@UtilityClass
public class UserAuthenticationBearer {

    public static Mono<Authentication> create(VerificationResult verificationResult) {
        Claims claims = verificationResult.getClaims();
        String subject = claims.getSubject();

        String role = claims.get("role", String.class);
        String username = claims.get("username", String.class);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        Long id = Long.parseLong(subject);

        CustomPrincipal principal = CustomPrincipal.builder()
                .id(id)
                .name(username)
                .build();

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
    }
}