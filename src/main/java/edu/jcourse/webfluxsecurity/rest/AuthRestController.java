package edu.jcourse.webfluxsecurity.rest;

import edu.jcourse.webfluxsecurity.dto.AuthRequestDto;
import edu.jcourse.webfluxsecurity.dto.AuthResponseDto;
import edu.jcourse.webfluxsecurity.dto.UserDto;
import edu.jcourse.webfluxsecurity.security.CustomPrincipal;
import edu.jcourse.webfluxsecurity.security.SecurityService;
import edu.jcourse.webfluxsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestController {
    private final UserService userService;
    private final SecurityService securityService;

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        return securityService.authenticate(authRequestDto.username(), authRequestDto.password())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .expireDate(tokenDetails.getExpireDate())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .token(tokenDetails.getToken())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        return userService.findById(((CustomPrincipal) authentication.getPrincipal()).getId());
    }
}