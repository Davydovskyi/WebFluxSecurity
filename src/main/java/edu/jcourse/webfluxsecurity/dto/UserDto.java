package edu.jcourse.webfluxsecurity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.jcourse.webfluxsecurity.entity.Role;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserDto(
        Long id,
        String username,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,
        Role role,
        String firstName,
        String lastName,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}