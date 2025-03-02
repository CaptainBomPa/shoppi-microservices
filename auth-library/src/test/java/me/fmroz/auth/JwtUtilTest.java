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
    void shouldGenerateAndValidateAccessToken() {
        AuthUserDetails userDetails = new AuthUserDetails("user@example.com", AccountType.USER);

        String accessToken = JwtUtil.generateAccessToken(userDetails);
        assertThat(accessToken).isNotNull();

        boolean isValid = JwtUtil.validateToken(accessToken, userDetails);
        assertThat(isValid).isTrue();

        String username = JwtUtil.extractUsername(accessToken);
        assertThat(username).isEqualTo("user@example.com");

        AccountType role = JwtUtil.extractUserRole(accessToken);
        assertThat(role).isEqualTo(AccountType.USER);
    }

    @Test
    void shouldFailForInvalidToken() {
        AuthUserDetails userDetails = new AuthUserDetails("user@example.com", AccountType.USER);
        String fakeToken = "invalidToken";

        boolean isValid = JwtUtil.validateToken(fakeToken, userDetails);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldGenerateAndValidateRefreshToken() {
        AuthUserDetails userDetails = new AuthUserDetails("user@example.com", AccountType.USER);

        String refreshToken = JwtUtil.generateRefreshToken(userDetails);
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken).isNotBlank();

        boolean isValid = JwtUtil.validateToken(refreshToken, userDetails);
        assertThat(isValid).isTrue();

        String username = JwtUtil.extractUsername(refreshToken);
        assertThat(username).isEqualTo("user@example.com");
    }
}
