package me.fmroz.shoppi.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.fmroz.shoppi.contract.product.Currency;
import me.fmroz.shoppi.contract.product.ProductStatus;
import me.fmroz.shoppi.product.dto.ProductRequest;
import me.fmroz.shoppi.product.model.Category;
import me.fmroz.shoppi.product.model.Product;
import me.fmroz.shoppi.product.repository.CategoryRepository;
import me.fmroz.shoppi.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ProductControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        testCategory = categoryRepository.save(Category.builder()
                .name("Electronics")
                .parentCategory(null)
                .build());
    }

    @Test
    void shouldCreateNewProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setTitle("Smartphone");
        productRequest.setDescription("High-end smartphone with OLED display.");
        productRequest.setExpiresAt(LocalDateTime.now().plusDays(30));
        productRequest.setUserId(1L);
        productRequest.setPrice(new BigDecimal("999.99"));
        productRequest.setCurrency(Currency.USD);
        productRequest.setStatus(ProductStatus.ACTIVE);
        productRequest.setQuantity(10);
        productRequest.setCategoryId(testCategory.getId());

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk());

        Product savedProduct = productRepository.findAll().get(0);
        assertThat(savedProduct.getTitle()).isEqualTo("Smartphone");
        assertThat(savedProduct.getDescription()).isEqualTo("High-end smartphone with OLED display.");
        assertThat(savedProduct.getPrice()).isEqualTo(new BigDecimal("999.99"));
        assertThat(savedProduct.getCurrency()).isEqualTo(Currency.USD);
        assertThat(savedProduct.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(savedProduct.getQuantity()).isEqualTo(10);
        assertThat(savedProduct.getCategory().getId()).isEqualTo(testCategory.getId());
    }

    @Test
    void shouldUpdateExistingProduct() throws Exception {
        Product existingProduct = productRepository.save(
                Product.builder()
                        .title("Old Laptop")
                        .description("Basic laptop")
                        .expiresAt(LocalDateTime.now().plusDays(10))
                        .userId(1L)
                        .price(new BigDecimal("499.99"))
                        .currency(Currency.EUR)
                        .status(ProductStatus.DRAFT)
                        .quantity(5)
                        .category(testCategory)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        ProductRequest updatedProductRequest = new ProductRequest();
        updatedProductRequest.setTitle("Gaming Laptop");
        updatedProductRequest.setDescription("Powerful gaming laptop with RTX GPU.");
        updatedProductRequest.setExpiresAt(LocalDateTime.now().plusDays(30));
        updatedProductRequest.setUserId(1L);
        updatedProductRequest.setPrice(new BigDecimal("1499.99"));
        updatedProductRequest.setCurrency(Currency.USD);
        updatedProductRequest.setStatus(ProductStatus.ACTIVE);
        updatedProductRequest.setQuantity(3);
        updatedProductRequest.setCategoryId(testCategory.getId());

        mockMvc.perform(put("/products/" + existingProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProductRequest)))
                .andExpect(status().isOk());

        Product updatedProduct = productRepository.findById(existingProduct.getId()).orElseThrow();
        assertThat(updatedProduct.getTitle()).isEqualTo("Gaming Laptop");
        assertThat(updatedProduct.getDescription()).isEqualTo("Powerful gaming laptop with RTX GPU.");
        assertThat(updatedProduct.getPrice()).isEqualTo(new BigDecimal("1499.99"));
        assertThat(updatedProduct.getCurrency()).isEqualTo(Currency.USD);
        assertThat(updatedProduct.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(updatedProduct.getQuantity()).isEqualTo(3);
    }

    @Test
    void shouldGetProductById() throws Exception {
        Product existingProduct = productRepository.save(
                Product.builder()
                        .title("Smartwatch")
                        .description("Waterproof smartwatch with AMOLED display.")
                        .expiresAt(LocalDateTime.now().plusDays(20))
                        .userId(2L)
                        .price(new BigDecimal("299.99"))
                        .currency(Currency.PLN)
                        .status(ProductStatus.ACTIVE)
                        .quantity(7)
                        .category(testCategory)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        mockMvc.perform(get("/products/" + existingProduct.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404ForNonExistingProduct() throws Exception {
        mockMvc.perform(get("/products/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetMultipleProductsByIds() throws Exception {
        Product product1 = productRepository.save(
                Product.builder()
                        .title("Phone A")
                        .description("First phone model")
                        .expiresAt(LocalDateTime.now().plusDays(20))
                        .userId(3L)
                        .price(new BigDecimal("599.99"))
                        .currency(Currency.USD)
                        .status(ProductStatus.ACTIVE)
                        .quantity(15)
                        .category(testCategory)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Product product2 = productRepository.save(
                Product.builder()
                        .title("Phone B")
                        .description("Second phone model")
                        .expiresAt(LocalDateTime.now().plusDays(15))
                        .userId(4L)
                        .price(new BigDecimal("499.99"))
                        .currency(Currency.USD)
                        .status(ProductStatus.ACTIVE)
                        .quantity(10)
                        .category(testCategory)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        mockMvc.perform(get("/products/batch?ids=" + product1.getId() + "," + product2.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404ForNonExistingProductsBatch() throws Exception {
        mockMvc.perform(get("/products/batch?ids=99999,88888"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteProductSuccessfully() throws Exception {
        Product product = productRepository.save(
                Product.builder()
                        .title("Old Camera")
                        .description("DSLR camera")
                        .expiresAt(LocalDateTime.now().plusDays(10))
                        .userId(5L)
                        .price(new BigDecimal("1200.00"))
                        .currency(Currency.EUR)
                        .status(ProductStatus.DRAFT)
                        .quantity(2)
                        .category(testCategory)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        mockMvc.perform(delete("/products/" + product.getId()))
                .andExpect(status().isNoContent());

        assertThat(productRepository.findById(product.getId())).isNotPresent();
    }

    @Test
    void shouldReturn404ForDeletingNonExistingProduct() throws Exception {
        mockMvc.perform(delete("/products/99999"))
                .andExpect(status().isNotFound());
    }
}
