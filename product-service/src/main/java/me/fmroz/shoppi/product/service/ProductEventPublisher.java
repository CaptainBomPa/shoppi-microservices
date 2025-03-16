package me.fmroz.shoppi.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.shoppi.product.dto.ProductEventDTO;
import me.fmroz.shoppi.product.model.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEventPublisher {

    private static final String EXCHANGE_NAME = "product.events";
    private final RabbitTemplate rabbitTemplate;

    public void sendProductCreatedEvent(Product product) {
        log.info("Sending product.created event for Product ID: {}", product.getId());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "product.created", mapProductToDto(product));
    }

    public void sendProductUpdatedEvent(Product product) {
        log.info("Sending product.updated event for Product ID: {}", product.getId());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "product.updated", mapProductToDto(product));
    }

    public void sendProductDeletedEvent(Long productId) {
        log.info("Sending product.deleted event for Product ID: {}", productId);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "product.deleted", productId);
    }

    private ProductEventDTO mapProductToDto(Product product) {
        return new ProductEventDTO(
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
        );
    }
}
