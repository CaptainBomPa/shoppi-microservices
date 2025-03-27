package me.fmroz.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.search.config.ProductSyncProperties;
import me.fmroz.search.dto.CachedProduct;
import me.fmroz.search.feign.ProductClient;
import me.fmroz.shoppi.contract.product.ProductEventDTO;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSyncService {

    private final ProductClient productClient;
    private final CachedProductService cachedProductService;
    private final ProductSyncProperties syncProperties;

    @EventListener(ApplicationReadyEvent.class)
    public void syncCacheAfterStartup() {
        if (!syncProperties.isSyncEnabled()) {
            log.info("Initial product sync is disabled via config.");
            return;
        }

        try {
            log.info("Attempting to sync product cache from product-service...");

            List<ProductEventDTO> products = productClient.getAllActiveProducts();
            int count = 0;

            for (ProductEventDTO dto : products) {
                CachedProduct cached = new CachedProduct(
                        dto.getId(),
                        dto.getTitle(),
                        dto.getDescription(),
                        dto.getPrice(),
                        dto.getCurrency().name(),
                        dto.getCategoryId(),
                        dto.getUserId(),
                        dto.getPromotedUntil()
                );
                cachedProductService.save(cached);
                count++;
            }

            log.info("Successfully synced {} products to Redis cache.", count);

        } catch (Exception e) {
            log.warn("Initial product sync failed: {}. Will retry in 15 seconds...", e.getMessage());
            retryLater();
        }
    }


    public void syncCacheSilently() {
        try {
            syncCacheAfterStartup();
        } catch (Exception e) {
            log.warn("Scheduled product sync failed: {}", e.getMessage());
        }
    }

    private void retryLater() {
        new Thread(() -> {
            try {
                Thread.sleep(15_000);
                syncCacheAfterStartup();
            } catch (InterruptedException ignored) {}
        }).start();
    }
}
