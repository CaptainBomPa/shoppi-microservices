package me.fmroz.search.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "product")
public class ProductSyncProperties {

    private boolean syncEnabled = true;
    private boolean scheduledSyncEnabled = true;
    private String syncInterval = "1h";

    public Duration getParsedInterval() {
        String input = syncInterval.trim().toLowerCase();

        if (input.endsWith("ms")) return Duration.ofMillis(Long.parseLong(input.replace("ms", "")));
        if (input.endsWith("s")) return Duration.ofSeconds(Long.parseLong(input.replace("s", "")));
        if (input.endsWith("m")) return Duration.ofMinutes(Long.parseLong(input.replace("m", "")));
        if (input.endsWith("h")) return Duration.ofHours(Long.parseLong(input.replace("h", "")));
        if (input.endsWith("d")) return Duration.ofDays(Long.parseLong(input.replace("d", "")));

        throw new IllegalArgumentException("Invalid syncInterval format: " + syncInterval);
    }
}

