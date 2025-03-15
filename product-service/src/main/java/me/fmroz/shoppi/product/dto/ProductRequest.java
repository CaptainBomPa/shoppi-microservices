package me.fmroz.shoppi.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import me.fmroz.shoppi.product.model.enums.Currency;
import me.fmroz.shoppi.product.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductRequest {

    @NotBlank
    @Size(min = 3, max = 255)
    private String title;

    @NotBlank
    @Size(min = 10)
    private String description;

    @NotNull
    private LocalDateTime expiresAt;

    @NotNull
    private Long userId;

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @NotNull
    private Currency currency;

    @NotNull
    private ProductStatus status;

    @Min(1)
    @Max(9999)
    private int quantity;

    @NotNull
    private Long categoryId;
}
