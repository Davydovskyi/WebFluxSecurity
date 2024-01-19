package edu.jcourse.webfluxsecurity.security;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

@Component
public class PBFDK2Encoder implements PasswordEncoder {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    @Value("${jwt.password.encoder.secret}")
    private String secret;
    @Value("${jwt.password.encoder.iterations}")
    private Integer iterations;
    @Value("${jwt.password.encoder.keyLength}")
    private Integer keyLength;

    @SneakyThrows
    @Override
    public String encode(CharSequence rawPassword) {
        byte[] result = SecretKeyFactory.getInstance(ALGORITHM)
                .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(), secret.getBytes(), iterations, keyLength))
                .getEncoded();
        return Base64.getEncoder().encodeToString(result);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
