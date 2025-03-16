package me.fmroz.shoppi.product.repository;

import me.fmroz.shoppi.product.model.Product;
import me.fmroz.shoppi.product.model.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatusAndExpiresAtBefore(ProductStatus status, LocalDateTime dateTime);

    List<Product> findAllByStatus(ProductStatus status);
}
