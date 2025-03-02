package me.fmroz.shoppi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.fmroz.auth.AccountType;
import me.fmroz.auth.JwtUtil;
import me.fmroz.shoppi.dto.LoginRequest;
import me.fmroz.shoppi.dto.LoginResponse;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.repository.ShoppiUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerE2ETest {

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "securepassword";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ShoppiUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterAndLoginUserSuccessfully() throws Exception {
        ShoppiUser user = ShoppiUser.builder()
                .email(TEST_EMAIL)
                .firstName("John")
                .lastName("Doe")
                .password(TEST_PASSWORD)
                .accountType(AccountType.USER)
                .registrationDate(ZonedDateTime.now())
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        ShoppiUser savedUser = userRepository.findByEmail(TEST_EMAIL).orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(savedUser.getPassword()).isEqualTo(TEST_PASSWORD);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(TEST_EMAIL);
        loginRequest.setPassword(TEST_PASSWORD);

        String loginResponseJson = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists()) // Sprawdzamy, czy zwrócono token
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse loginResponse = objectMapper.readValue(loginResponseJson, LoginResponse.class);
        assertThat(loginResponse.getToken()).isNotBlank();
        assertThat(JwtUtil.validateToken(loginResponse.getToken(), new me.fmroz.auth.AuthUserDetails(TEST_EMAIL, AccountType.USER))).isTrue();
    }
}
