package me.fmroz.shoppi.product.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.fmroz.shoppi.contract.product.Currency;
import me.fmroz.shoppi.contract.product.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
