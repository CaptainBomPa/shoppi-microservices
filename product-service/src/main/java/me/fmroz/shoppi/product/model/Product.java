package me.fmroz.shoppi.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import me.fmroz.shoppi.product.model.enums.Currency;
import me.fmroz.shoppi.product.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Size(min = 10, message = "Description must have at least 10 characters")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 2 decimal places")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 9999, message = "Quantity cannot exceed 9999")
    private int quantity;

    @Column
    private LocalDateTime promotedUntil;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
