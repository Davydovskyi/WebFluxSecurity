package edu.jcourse.webfluxsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebFluxSecurityApplication {
    public static void main(String[] args) {
        new SpringApplication(WebFluxSecurityApplication.class).run(args);
    }
}
