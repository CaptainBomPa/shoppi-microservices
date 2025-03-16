package me.fmroz.shoppi.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.shoppi.product.model.Product;
import me.fmroz.shoppi.product.model.enums.ProductStatus;
import me.fmroz.shoppi.product.repository.ProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductExpirationService {

    private final ProductRepository productRepository;
    private final ProductEventPublisher productEventPublisher;

    @Scheduled(fixedRate = 60000)
    public void expireOldProducts() {
        LocalDateTime now = LocalDateTime.now();
        List<Product> expiredProducts = productRepository.findByStatusAndExpiresAtBefore(ProductStatus.ACTIVE, now);

        if (expiredProducts.isEmpty()) {
            return;
        }

        log.info("Expiring {} products...", expiredProducts.size());

        expiredProducts.forEach(product -> {
            product.setStatus(ProductStatus.EXPIRED);
            productEventPublisher.sendProductUpdatedEvent(product);
        });

        productRepository.saveAll(expiredProducts);
    }
}
