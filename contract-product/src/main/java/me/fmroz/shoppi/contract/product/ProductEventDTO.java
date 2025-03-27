package me.fmroz.shoppi.contract.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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