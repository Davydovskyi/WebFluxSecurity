package edu.jcourse.webfluxsecurity.service;

import edu.jcourse.webfluxsecurity.dto.UserDto;
import edu.jcourse.webfluxsecurity.entity.Role;
import edu.jcourse.webfluxsecurity.mapper.UserMapper;
import edu.jcourse.webfluxsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public Mono<UserDto> register(UserDto user) {
        UserDto userDto = user.toBuilder()
                .password(passwordEncoder.encode(user.password()))
                .role(Role.USER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return userRepository.save(userMapper.dtoToEntity(userDto))
                .map(userMapper::entityToDto);
    }

    public Mono<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::entityToDto);
    }

    public Mono<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::entityToDto);
    }
}