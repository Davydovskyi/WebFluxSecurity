package edu.jcourse.webfluxsecurity.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String BEARER = "Bearer ";
    private static final Function<String, Mono<String>> BEARER_TOKEN = token -> Mono.just(token.substring(BEARER.length()));
    private final JwtHandler jwtHandler;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return getBearerToken(exchange)
                .flatMap(BEARER_TOKEN)
                .flatMap(jwtHandler::checkToken)
                .flatMap(UserAuthenticationBearer::create);
    }

    private Mono<String> getBearerToken(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
