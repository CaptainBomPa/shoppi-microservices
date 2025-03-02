package me.fmroz.shoppi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.fmroz.shoppi.config.TestSecurityConfig;
import me.fmroz.shoppi.model.CompanyInfo;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.model.staticdata.AccountType;
import me.fmroz.shoppi.repository.CompanyInfoRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
public class CompanyInfoControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    private ShoppiUserRepository userRepository;

    private ShoppiUser testUser;

    @BeforeEach
    void setUp() {
        companyInfoRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(
                ShoppiUser.builder()
                        .email("user@example.com")
                        .firstName("John")
                        .lastName("Doe")
                        .password("securepassword")
                        .accountType(AccountType.SELLER)
                        .registrationDate(ZonedDateTime.now())
                        .build()
        );
    }

    @Test
    void shouldAddNewCompanyInfo() throws Exception {
        CompanyInfo companyInfo = CompanyInfo.builder()
                .companyName("Tech Corp")
                .postalCode("12-345")
                .city("New York")
                .street("Wall Street 1")
                .country("USA")
                .phone("+123456789")
                .build();

        mockMvc.perform(post("/company-info/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyInfo)))
                .andExpect(status().isOk());

        CompanyInfo savedCompanyInfo = companyInfoRepository.findByUserId(testUser.getId()).orElseThrow();
        assertThat(savedCompanyInfo.getCompanyName()).isEqualTo("Tech Corp");
        assertThat(savedCompanyInfo.getPostalCode()).isEqualTo("12-345");
        assertThat(savedCompanyInfo.getCity()).isEqualTo("New York");
        assertThat(savedCompanyInfo.getStreet()).isEqualTo("Wall Street 1");
        assertThat(savedCompanyInfo.getCountry()).isEqualTo("USA");
        assertThat(savedCompanyInfo.getPhone()).isEqualTo("+123456789");
    }

    @Test
    void shouldFailWhenAddingCompanyInfoIfAlreadyExists() throws Exception {
        ShoppiUser persistedUser = userRepository.findById(testUser.getId()).orElseThrow();
        persistedUser.setCompanyInfo(CompanyInfo.builder()
                .user(persistedUser)
                .companyName("Tech Corp")
                .postalCode("12-345")
                .city("New York")
                .street("Wall Street 1")
                .country("USA")
                .phone("+123456789")
                .build());
        userRepository.save(persistedUser);

        CompanyInfo newCompanyInfo = CompanyInfo.builder()
                .companyName("Another Corp")
                .postalCode("98-765")
                .city("Los Angeles")
                .street("Hollywood Blvd 20")
                .country("USA")
                .phone("+1987654321")
                .build();

        mockMvc.perform(post("/company-info/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCompanyInfo)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldUpdateExistingCompanyInfo() throws Exception {
        ShoppiUser persistedUser = userRepository.findById(testUser.getId()).orElseThrow();

        companyInfoRepository.save(
                CompanyInfo.builder()
                        .user(persistedUser)
                        .companyName("Tech Corp")
                        .postalCode("12-345")
                        .city("New York")
                        .street("Wall Street 1")
                        .country("USA")
                        .phone("+123456789")
                        .build()
        );

        CompanyInfo updatedCompanyInfo = CompanyInfo.builder()
                .companyName("Updated Tech Corp")
                .postalCode("99-999")
                .city("San Francisco")
                .street("Market Street 20")
                .country("USA")
                .phone("+1987654321")
                .build();

        mockMvc.perform(put("/company-info/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCompanyInfo)))
                .andExpect(status().isOk());

        CompanyInfo updatedFromDb = companyInfoRepository.findByUserId(testUser.getId()).orElseThrow();
        assertThat(updatedFromDb.getCompanyName()).isEqualTo("Updated Tech Corp");
        assertThat(updatedFromDb.getPostalCode()).isEqualTo("99-999");
        assertThat(updatedFromDb.getCity()).isEqualTo("San Francisco");
        assertThat(updatedFromDb.getStreet()).isEqualTo("Market Street 20");
        assertThat(updatedFromDb.getCountry()).isEqualTo("USA");
        assertThat(updatedFromDb.getPhone()).isEqualTo("+1987654321");
    }

    @Test
    void shouldFailWhenUpdatingCompanyInfoIfDoesNotExist() throws Exception {
        CompanyInfo updatedCompanyInfo = CompanyInfo.builder()
                .companyName("Updated Tech Corp")
                .postalCode("99-999")
                .city("San Francisco")
                .street("Market Street 20")
                .country("USA")
                .phone("+1987654321")
                .build();

        mockMvc.perform(put("/company-info/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCompanyInfo)))
                .andExpect(status().isNotFound());
    }
}
