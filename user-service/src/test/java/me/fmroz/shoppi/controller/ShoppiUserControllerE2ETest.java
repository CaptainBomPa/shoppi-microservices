package me.fmroz.shoppi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.fmroz.auth.AccountType;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.model.staticdata.Gender;
import me.fmroz.shoppi.repository.ShoppiUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ShoppiUserControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShoppiUserRepository userRepository;

    private final String RAW_PASSWORD = "securepassword";
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        ShoppiUser user = ShoppiUser.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password(RAW_PASSWORD)
                .gender(Gender.MALE)
                .accountType(AccountType.USER)
                .registrationDate(ZonedDateTime.now())
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        ShoppiUser savedUser = userRepository.findByEmail("test@example.com").orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getFirstName()).isEqualTo("John");
        assertThat(savedUser.getLastName()).isEqualTo("Doe");
        assertThat(savedUser.getGender()).isEqualTo(Gender.MALE);
        assertThat(savedUser.getAccountType()).isEqualTo(AccountType.USER);
        assertThat(savedUser.getPassword()).isNotEqualTo(RAW_PASSWORD);
        assertThat(passwordEncoder.matches(RAW_PASSWORD, savedUser.getPassword())).isTrue();
    }

    @Test
    void shouldCreateUserWithOnlyRequiredFields() throws Exception {
        ShoppiUser user = ShoppiUser.builder()
                .email("minimal@example.com")
                .firstName("Anna")
                .lastName("Smith")
                .password(RAW_PASSWORD)
                .accountType(AccountType.USER)
                .registrationDate(ZonedDateTime.now())
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        ShoppiUser savedUser = userRepository.findByEmail("minimal@example.com").orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo("minimal@example.com");
        assertThat(savedUser.getFirstName()).isEqualTo("Anna");
        assertThat(savedUser.getLastName()).isEqualTo("Smith");
        assertThat(savedUser.getGender()).isEqualTo(Gender.UNKNOWN);
        assertThat(savedUser.getAccountType()).isEqualTo(AccountType.USER);
        assertThat(savedUser.getPassword()).isNotEqualTo(RAW_PASSWORD);
        assertThat(passwordEncoder.matches(RAW_PASSWORD, savedUser.getPassword())).isTrue();
    }

    @Test
    void shouldUpdateExistingUser() throws Exception {
        ShoppiUser user = userRepository.save(
                ShoppiUser.builder()
                        .email("existing@example.com")
                        .firstName("Mike")
                        .lastName("Brown")
                        .password(passwordEncoder.encode("oldpassword"))
                        .gender(Gender.MALE)
                        .accountType(AccountType.USER)
                        .registrationDate(ZonedDateTime.now())
                        .build()
        );

        ShoppiUser updatedUser = ShoppiUser.builder()
                .email("updated@example.com")
                .firstName("Michael")
                .lastName("Brown Jr.")
                .gender(Gender.MALE)
                .accountType(AccountType.USER)
                .build();

        mockMvc.perform(put("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk());

        ShoppiUser updatedFromDb = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedFromDb.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedFromDb.getFirstName()).isEqualTo("Michael");
        assertThat(updatedFromDb.getLastName()).isEqualTo("Brown Jr.");
        assertThat(updatedFromDb.getGender()).isEqualTo(Gender.MALE);
        assertThat(updatedFromDb.getAccountType()).isEqualTo(AccountType.USER);
        assertThat(updatedFromDb.getPassword()).isEqualTo(user.getPassword());
    }
}
