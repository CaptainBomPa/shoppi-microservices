package me.fmroz.search.config;

import com.github.fridujo.rabbitmq.mock.spring.MockConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.junit.RabbitAvailable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RabbitAvailable
public class RabbitMQTestConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new MockConnectionFactory();
    }
}
