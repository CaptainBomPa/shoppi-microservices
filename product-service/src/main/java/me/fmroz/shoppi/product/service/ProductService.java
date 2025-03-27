package me.fmroz.shoppi.product.service;

import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.product.dto.ProductEventDTO;
import me.fmroz.shoppi.product.dto.ProductRequest;
import me.fmroz.shoppi.product.exception.CategoryNotFoundException;
import me.fmroz.shoppi.product.exception.ProductNotFoundException;
import me.fmroz.shoppi.product.model.Category;
import me.fmroz.shoppi.product.model.Product;
import me.fmroz.shoppi.product.model.enums.ProductStatus;
import me.fmroz.shoppi.product.repository.CategoryRepository;
import me.fmroz.shoppi.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductEventPublisher productEventPublisher;

    @Transactional
    public Product createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = Product.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .expiresAt(request.getExpiresAt())
                .userId(request.getUserId())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .status(request.getStatus())
                .quantity(request.getQuantity())
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);
        if (request.getStatus() != ProductStatus.DRAFT) {
            productEventPublisher.sendProductCreatedEvent(savedProduct);
        }

        return savedProduct;
    }

    @Transactional
    public Product updateProduct(Long productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setExpiresAt(request.getExpiresAt());
        product.setPrice(request.getPrice());
        product.setCurrency(request.getCurrency());
        product.setStatus(request.getStatus());
        product.setQuantity(request.getQuantity());
        product.setCategory(categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + request.getCategoryId())));

        Product updatedProduct = productRepository.save(product);
        if (request.getStatus() != ProductStatus.DRAFT) {
            productEventPublisher.sendProductUpdatedEvent(updatedProduct);
        }

        return updatedProduct;
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    public List<Product> getProductsByIds(Set<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found for given IDs.");
        }
        return products;
    }

    public List<ProductEventDTO> getAllActiveProducts() {
        return productRepository.findAllByStatus(ProductStatus.ACTIVE).stream()
                .map(product -> new ProductEventDTO(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCurrency(),
                        product.getCategory().getId(),
                        product.getUserId(),
                        product.getStatus(),
                        product.getExpiresAt(),
                        product.getPromotedUntil()
                ))
                .toList();
    }


    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
        productEventPublisher.sendProductDeletedEvent(productId);
    }
}
