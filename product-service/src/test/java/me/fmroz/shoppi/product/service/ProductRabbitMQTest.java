package me.fmroz.shoppi.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.fmroz.shoppi.product.config.RabbitMQTestConfig;
import me.fmroz.shoppi.product.dto.ProductEventDTO;
import me.fmroz.shoppi.product.dto.ProductRequest;
import me.fmroz.shoppi.product.model.Category;
import me.fmroz.shoppi.product.model.Product;
import me.fmroz.shoppi.product.model.enums.Currency;
import me.fmroz.shoppi.product.model.enums.ProductStatus;
import me.fmroz.shoppi.product.repository.CategoryRepository;
import me.fmroz.shoppi.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(RabbitMQTestConfig.class)
@TestPropertySource(properties = {
        "spring.rabbitmq.host=embedded",
        "spring.rabbitmq.port=5672"
})
public class ProductRabbitMQTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductExpirationService productExpirationService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Category category;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        category = categoryRepository.save(Category.builder().name("Test Category").build());
    }

    @Test
    void shouldSendProductCreatedMessageForActiveProduct() throws Exception {
        ProductRequest request = new ProductRequest(
                "Active Product",
                "Test Description",
                LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                1L,
                new BigDecimal("199.99"),
                Currency.USD,
                ProductStatus.ACTIVE,
                10,
                category.getId()
        );

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Message message = await().atMost(5, TimeUnit.SECONDS)
                .until(() -> rabbitTemplate.receive("product.created.queue"), Objects::nonNull);

        assertThat(message).isNotNull();
        ProductEventDTO receivedEvent = objectMapper.readValue(new String(message.getBody()), ProductEventDTO.class);

        assertThat(receivedEvent.getTitle()).isEqualTo("Active Product");
        assertThat(receivedEvent.getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    void shouldNotSendMessageForDraftProduct() throws Exception {
        ProductRequest request = new ProductRequest(
                "Draft Product",
                "Test Description",
                LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                1L,
                new BigDecimal("199.99"),
                Currency.USD,
                ProductStatus.DRAFT,
                10,
                category.getId()
        );

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Message message = rabbitTemplate.receive("product.created.queue");
        assertThat(message).isNull();
    }

    @Test
    void shouldSendUpdateMessageWhenDraftBecomesActive() throws Exception {
        Product product = productRepository.save(Product.builder()
                .title("Draft Product")
                .description("Test Description")
                .expiresAt(LocalDateTime.of(2025, 12, 31, 23, 59, 59))
                .userId(1L)
                .price(new BigDecimal("199.99"))
                .currency(Currency.USD)
                .status(ProductStatus.DRAFT)
                .quantity(10)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build());

        ProductRequest updateRequest = new ProductRequest(
                "Draft Product",
                "Test Description",
                LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                1L,
                new BigDecimal("199.99"),
                Currency.USD,
                ProductStatus.ACTIVE,
                10,
                category.getId()
        );

        mockMvc.perform(put("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        Message message = await().atMost(5, TimeUnit.SECONDS)
                .until(() -> rabbitTemplate.receive("product.updated.queue"), Objects::nonNull);

        assertThat(message).isNotNull();
        ProductEventDTO receivedEvent = objectMapper.readValue(new String(message.getBody()), ProductEventDTO.class);
        assertThat(receivedEvent.getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    void shouldSendDeleteMessageWhenProductIsDeleted() throws Exception {
        Product product = productRepository.save(Product.builder()
                .title("To Be Deleted")
                .description("Test Description")
                .expiresAt(LocalDateTime.of(2025, 12, 31, 23, 59, 59))
                .userId(1L)
                .price(new BigDecimal("199.99"))
                .currency(Currency.USD)
                .status(ProductStatus.ACTIVE)
                .quantity(10)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build());

        mockMvc.perform(delete("/products/" + product.getId()))
                .andExpect(status().isNoContent());

        Message message = await().atMost(5, TimeUnit.SECONDS)
                .until(() -> rabbitTemplate.receive("product.deleted.queue"), Objects::nonNull);

        assertThat(message).isNotNull();
        assertThat(new String(message.getBody())).contains(product.getId().toString());
    }

    @Test
    void shouldSendUpdateMessageWhenProductExpires() throws Exception {
        productRepository.save(Product.builder()
                .title("Expiring Product")
                .description("Test Description")
                .expiresAt(LocalDateTime.now().minusMinutes(10))
                .userId(1L)
                .price(new BigDecimal("199.99"))
                .currency(Currency.USD)
                .status(ProductStatus.ACTIVE)
                .quantity(10)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build());

        productExpirationService.expireOldProducts();

        Message message = await().atMost(5, TimeUnit.SECONDS)
                .until(() -> rabbitTemplate.receive("product.updated.queue"), Objects::nonNull);

        assertThat(message).isNotNull();
        ProductEventDTO receivedEvent = objectMapper.readValue(new String(message.getBody()), ProductEventDTO.class);
        assertThat(receivedEvent.getStatus()).isEqualTo(ProductStatus.EXPIRED);
    }
}
