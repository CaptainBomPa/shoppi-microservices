package me.fmroz.auth;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtUtilTest {

    private static final String TEST_SECRET = "testSecretKeyForJwtTesting1234567890!";

    @BeforeAll
    static void setup() {
        System.setProperty("JWT_SECRET", TEST_SECRET);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        AuthUserDetails userDetails = new AuthUserDetails("user@example.com", AccountType.USER);

        String token = JwtUtil.generateToken(userDetails);
        assertThat(token).isNotNull();

        boolean isValid = JwtUtil.validateToken(token, userDetails);
        assertThat(isValid).isTrue();

        String username = JwtUtil.extractUsername(token);
        assertThat(username).isEqualTo("user@example.com");

        AccountType role = JwtUtil.extractUserRole(token);
        assertThat(role).isEqualTo(AccountType.USER);
    }

    @Test
    void shouldFailForInvalidToken() {
        AuthUserDetails userDetails = new AuthUserDetails("user@example.com", AccountType.USER);
        String fakeToken = "invalidToken";

        boolean isValid = JwtUtil.validateToken(fakeToken, userDetails);
        assertThat(isValid).isFalse();
    }
}
