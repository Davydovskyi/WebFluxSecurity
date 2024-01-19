package edu.jcourse.webfluxsecurity.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomPrincipal implements Principal {
    private Long id;
    private String name;
}