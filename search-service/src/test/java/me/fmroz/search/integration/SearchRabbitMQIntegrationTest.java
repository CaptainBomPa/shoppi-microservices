package me.fmroz.search.integration;

import me.fmroz.search.config.RabbitMQTestConfig;
import me.fmroz.search.dto.CachedProduct;
import me.fmroz.search.service.CachedProductService;
import me.fmroz.shoppi.contract.product.Currency;
import me.fmroz.shoppi.contract.product.ProductEventDTO;
import me.fmroz.shoppi.contract.product.ProductStatus;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(RabbitMQTestConfig.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SearchRabbitMQIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CachedProductService cachedProductService;

    private RedisServer redisServer;

    private static final Long TEST_PRODUCT_ID = 101L;

    @BeforeAll
    void startRedis() {
        redisServer = new RedisServer(6379);
        try {
            redisServer.start();
        } catch (Exception e) {
            System.out.println("Failed to start Redis Server");
            e.printStackTrace();
        }
    }

    @AfterAll
    void stopRedis() {
        redisServer.stop();
    }

    @BeforeEach
    void clearRedis() {
        cachedProductService.delete(TEST_PRODUCT_ID);
    }

    @Test
    void shouldStoreProductInRedisOnProductCreatedEvent() throws Exception {
        ProductEventDTO event = createTestProduct("Created Product");

        rabbitTemplate.convertAndSend("product.events", "product.created", event);
        Thread.sleep(500);

        CachedProduct cached = cachedProductService.findById(TEST_PRODUCT_ID);
        assertThat(cached).isNotNull();
        assertThat(cached.getTitle()).isEqualTo("Created Product");
    }

    @Test
    void shouldUpdateProductInRedisOnProductUpdatedEvent() throws Exception {
        ProductEventDTO initial = createTestProduct("Initial");
        ProductEventDTO updated = createTestProduct("Updated Title");

        rabbitTemplate.convertAndSend("product.events", "product.created", initial);
        Thread.sleep(300);

        rabbitTemplate.convertAndSend("product.events", "product.updated", updated);
        Thread.sleep(500);

        CachedProduct cached = cachedProductService.findById(TEST_PRODUCT_ID);
        assertThat(cached).isNotNull();
        assertThat(cached.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void shouldRemoveProductFromRedisOnProductDeletedEvent() throws Exception {
        ProductEventDTO event = createTestProduct("To Be Deleted");

        rabbitTemplate.convertAndSend("product.events", "product.created", event);
        Thread.sleep(300);

        rabbitTemplate.convertAndSend("product.events", "product.deleted", TEST_PRODUCT_ID);
        Thread.sleep(500);

        CachedProduct cached = cachedProductService.findById(TEST_PRODUCT_ID);
        assertThat(cached).isNull();
    }

    @Test
    void shouldNotStoreNonActiveProductOnCreate() throws Exception {
        ProductEventDTO event = createTestProduct("Draft Product", ProductStatus.DRAFT);

        rabbitTemplate.convertAndSend("product.events", "product.created", event);
        Thread.sleep(500);

        CachedProduct cached = cachedProductService.findById(TEST_PRODUCT_ID);
        assertThat(cached).isNull();
    }

    @Test
    void shouldDeleteProductFromRedisOnUpdateToNonActive() throws Exception {
        ProductEventDTO active = createTestProduct("Active Product", ProductStatus.ACTIVE);
        ProductEventDTO draft = createTestProduct("Now Draft", ProductStatus.DRAFT);

        rabbitTemplate.convertAndSend("product.events", "product.created", active);
        Thread.sleep(300);

        rabbitTemplate.convertAndSend("product.events", "product.updated", draft);
        Thread.sleep(500);

        CachedProduct cached = cachedProductService.findById(TEST_PRODUCT_ID);
        assertThat(cached).isNull();
    }

    @Test
    void shouldStoreProductInRedisOnUpdateToActive() throws Exception {
        ProductEventDTO draft = createTestProduct("Initially Draft", ProductStatus.DRAFT);
        ProductEventDTO active = createTestProduct("Now Active", ProductStatus.ACTIVE);

        rabbitTemplate.convertAndSend("product.events", "product.created", draft);
        Thread.sleep(300);

        rabbitTemplate.convertAndSend("product.events", "product.updated", active);
        Thread.sleep(500);

        CachedProduct cached = cachedProductService.findById(TEST_PRODUCT_ID);
        assertThat(cached).isNotNull();
        assertThat(cached.getTitle()).isEqualTo("Now Active");
    }

    private ProductEventDTO createTestProduct(String title) {
        return createTestProduct(title, ProductStatus.ACTIVE);
    }

    private ProductEventDTO createTestProduct(String title, ProductStatus status) {
        return new ProductEventDTO(
                TEST_PRODUCT_ID,
                title,
                "Test Desc",
                BigDecimal.valueOf(99.99),
                Currency.PLN,
                10L,
                1L,
                status,
                LocalDateTime.now().plusDays(10),
                null
        );
    }
}
