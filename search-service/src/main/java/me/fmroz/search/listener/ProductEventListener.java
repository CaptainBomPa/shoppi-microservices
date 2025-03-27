package me.fmroz.search.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.search.dto.CachedProduct;
import me.fmroz.search.service.CachedProductService;
import me.fmroz.shoppi.contract.product.ProductEventDTO;
import me.fmroz.shoppi.contract.product.ProductStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventListener {

    private final CachedProductService cachedProductService;

    @RabbitListener(queues = "product.created.queue")
    public void handleProductCreated(ProductEventDTO event) {
        log.info("Received product.created event: {}", event);

        if (event.getStatus() == ProductStatus.ACTIVE) {
            CachedProduct cachedProduct = mapToCachedProduct(event);
            cachedProductService.save(cachedProduct);
        } else {
            log.info("Product with ID {} is not ACTIVE. Skipping cache save.", event.getId());
        }
    }

    @RabbitListener(queues = "product.updated.queue")
    public void handleProductUpdated(ProductEventDTO event) {
        log.info("Received product.updated event: {}", event);

        if (event.getStatus() == ProductStatus.ACTIVE) {
            CachedProduct cachedProduct = mapToCachedProduct(event);
            cachedProductService.save(cachedProduct);
        } else {
            cachedProductService.delete(event.getId());
        }
    }

    @RabbitListener(queues = "product.deleted.queue")
    public void handleProductDeleted(Long productId) {
        log.info("Received product.deleted event for ID: {}", productId);
        cachedProductService.delete(productId);
    }

    private CachedProduct mapToCachedProduct(ProductEventDTO dto) {
        return new CachedProduct(
                dto.getId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getCurrency().name(),
                dto.getCategoryId(),
                dto.getUserId()
        );
    }
}
