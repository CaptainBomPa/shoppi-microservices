package me.fmroz.shoppi.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ComponentScan(basePackages = "me.fmroz.shoppi")
@ActiveProfiles("test")
public class TestConfig {
}
