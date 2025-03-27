package me.fmroz.search.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.fmroz.search.dto.CachedProduct;
import me.fmroz.search.service.CachedProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import redis.embedded.RedisServer;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductCacheSearchE2ETest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CachedProductService cachedProductService;

    private RedisServer redisServer;

    @BeforeAll
    void startRedis() {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterAll
    void stopRedis() {
        redisServer.stop();
    }

    @BeforeEach
    void setup() {
        cachedProductService.findAll().forEach(p -> cachedProductService.delete(p.getId()));

        List.of(
                new CachedProduct(1L, "Laptop X", "Gaming laptop", new BigDecimal("1500"), "PLN", 3L, 1L, null),
                new CachedProduct(2L, "Budget Laptop", "Basic device", new BigDecimal("900"), "PLN", 3L, 2L, null),
                new CachedProduct(3L, "Smartphone", "Android phone", new BigDecimal("1300"), "USD", 4L, 1L, null),
                new CachedProduct(4L, "Laptop Bag", "Accessory", new BigDecimal("200"), "PLN", 5L, 3L, null)
        ).forEach(cachedProductService::save);
    }

    @Test
    void shouldReturnProductsByText() throws Exception {
        mockMvc.perform(get("/cache/products/search?text=Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnProductsWithinPriceRange() throws Exception {
        mockMvc.perform(get("/cache/products/search?minPrice=1000&maxPrice=1600"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnProductsByCurrency() throws Exception {
        mockMvc.perform(get("/cache/products/search?currency=USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Smartphone"));
    }

    @Test
    void shouldReturnProductsByCategoryId() throws Exception {
        mockMvc.perform(get("/cache/products/search?categoryId=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnProductsByUserId() throws Exception {
        mockMvc.perform(get("/cache/products/search?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnProductsMatchingMultipleCriteria() throws Exception {
        mockMvc.perform(get("/cache/products/search?text=Laptop&minPrice=1000&maxPrice=1600&currency=PLN&categoryId=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldReturnBadRequestWhenNoParamsProvided() throws Exception {
        mockMvc.perform(get("/cache/products/search"))
                .andExpect(status().isBadRequest());
    }
}
