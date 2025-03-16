package me.fmroz.shoppi.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.fmroz.shoppi.product.model.enums.Currency;
import me.fmroz.shoppi.product.model.enums.ProductStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEventDTO implements Serializable {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Currency currency;
    private Long categoryId;
    private Long userId;
    private ProductStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime promotedUntil;
}
