package me.fmroz.shoppi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.fmroz.auth.AccountType;
import me.fmroz.shoppi.model.ShippingInfo;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.repository.ShippingInfoRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ShippingInfoControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    @Autowired
    private ShoppiUserRepository userRepository;

    private ShoppiUser testUser;

    @BeforeEach
    void setUp() {
        shippingInfoRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(
                ShoppiUser.builder()
                        .email("user@example.com")
                        .firstName("John")
                        .lastName("Doe")
                        .password("securepassword")
                        .accountType(AccountType.USER)
                        .registrationDate(ZonedDateTime.now())
                        .build()
        );
    }

    @Test
    void shouldAddNewShippingInfo() throws Exception {
        ShippingInfo shippingInfo = ShippingInfo.builder()
                .postalCode("12-345")
                .city("New York")
                .street("5th Avenue 10")
                .country("USA")
                .phone("+1234567890")
                .countryCode("+1")
                .build();

        mockMvc.perform(post("/shipping-info/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shippingInfo)))
                .andExpect(status().isOk());

        ShippingInfo savedShippingInfo = shippingInfoRepository.findAll().get(0);
        assertThat(savedShippingInfo.getPostalCode()).isEqualTo("12-345");
        assertThat(savedShippingInfo.getCity()).isEqualTo("New York");
        assertThat(savedShippingInfo.getStreet()).isEqualTo("5th Avenue 10");
        assertThat(savedShippingInfo.getCountry()).isEqualTo("USA");
        assertThat(savedShippingInfo.getPhone()).isEqualTo("+1234567890");
        assertThat(savedShippingInfo.getCountryCode()).isEqualTo("+1");
        assertThat(savedShippingInfo.getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void shouldUpdateExistingShippingInfo() throws Exception {
        ShippingInfo existingShippingInfo = shippingInfoRepository.save(
                ShippingInfo.builder()
                        .user(testUser)
                        .postalCode("12-345")
                        .city("New York")
                        .street("5th Avenue 10")
                        .country("USA")
                        .phone("+1234567890")
                        .countryCode("+1")
                        .build()
        );

        ShippingInfo updatedShippingInfo = ShippingInfo.builder()
                .postalCode("98-765")
                .city("Los Angeles")
                .street("Hollywood Blvd 20")
                .country("USA")
                .phone("+1987654321")
                .countryCode("+1")
                .build();

        mockMvc.perform(put("/shipping-info/" + testUser.getId() + "/" + existingShippingInfo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedShippingInfo)))
                .andExpect(status().isOk());

        ShippingInfo updatedFromDb = shippingInfoRepository.findById(existingShippingInfo.getId()).orElseThrow();
        assertThat(updatedFromDb.getPostalCode()).isEqualTo("98-765");
        assertThat(updatedFromDb.getCity()).isEqualTo("Los Angeles");
        assertThat(updatedFromDb.getStreet()).isEqualTo("Hollywood Blvd 20");
        assertThat(updatedFromDb.getCountry()).isEqualTo("USA");
        assertThat(updatedFromDb.getPhone()).isEqualTo("+1987654321");
        assertThat(updatedFromDb.getCountryCode()).isEqualTo("+1");
    }

    @Test
    void shouldFailWhenMissingFieldsInShippingInfo() throws Exception {
        ShippingInfo incompleteShippingInfo = ShippingInfo.builder()
                .postalCode("")
                .city("Los Angeles")
                .street("")
                .country("USA")
                .phone("+1987654321")
                .countryCode("+1")
                .build();

        mockMvc.perform(post("/shipping-info/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteShippingInfo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWhenAddingShippingInfoForNonExistingUser() throws Exception {
        ShippingInfo shippingInfo = ShippingInfo.builder()
                .postalCode("12-345")
                .city("New York")
                .street("5th Avenue 10")
                .country("USA")
                .phone("+1234567890")
                .countryCode("+1")
                .build();

        mockMvc.perform(post("/shipping-info/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shippingInfo)))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldDeleteShippingInfoSuccessfully() throws Exception {
        ShippingInfo existingShippingInfo = shippingInfoRepository.save(
                ShippingInfo.builder()
                        .user(testUser)
                        .postalCode("12-345")
                        .city("New York")
                        .street("5th Avenue 10")
                        .country("USA")
                        .phone("+1234567890")
                        .countryCode("+1")
                        .build()
        );

        mockMvc.perform(delete("/shipping-info/" + testUser.getId() + "/" + existingShippingInfo.getId()))
                .andExpect(status().isNoContent());

        assertThat(shippingInfoRepository.findById(existingShippingInfo.getId())).isNotPresent();
    }
}
