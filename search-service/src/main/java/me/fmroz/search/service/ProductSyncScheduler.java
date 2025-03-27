package me.fmroz.search.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.search.config.ProductSyncProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductSyncScheduler {

    private final ProductSyncService productSyncService;
    private final ProductSyncProperties syncProperties;

    private final AtomicLong intervalMillis = new AtomicLong();

    @PostConstruct
    public void init() {
        Duration interval = syncProperties.getParsedInterval();
        intervalMillis.set(interval.toMillis());
        log.info("Product sync scheduler initialized with interval: {}", interval);
    }

    @Scheduled(fixedDelayString = "#{@productSyncScheduler.getIntervalMillis()}")
    public void scheduledSync() {
        if (!syncProperties.isScheduledSyncEnabled()) {
            return;
        }
        log.info("Running scheduled product sync...");
        productSyncService.syncCacheSilently();
    }


    public long getIntervalMillis() {
        return intervalMillis.get();
    }
}
