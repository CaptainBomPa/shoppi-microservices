package me.fmroz.search.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUCT_EXCHANGE = "product.events";

    public static final String PRODUCT_CREATED_QUEUE = "product.created.queue";
    public static final String PRODUCT_UPDATED_QUEUE = "product.updated.queue";
    public static final String PRODUCT_DELETED_QUEUE = "product.deleted.queue";

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public Queue productCreatedQueue() {
        return new Queue(PRODUCT_CREATED_QUEUE, true);
    }

    @Bean
    public Queue productUpdatedQueue() {
        return new Queue(PRODUCT_UPDATED_QUEUE, true);
    }

    @Bean
    public Queue productDeletedQueue() {
        return new Queue(PRODUCT_DELETED_QUEUE, true);
    }

    @Bean
    public Binding bindingCreated(Queue productCreatedQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productCreatedQueue).to(productExchange).with("product.created");
    }

    @Bean
    public Binding bindingUpdated(Queue productUpdatedQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productUpdatedQueue).to(productExchange).with("product.updated");
    }

    @Bean
    public Binding bindingDeleted(Queue productDeletedQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productDeletedQueue).to(productExchange).with("product.deleted");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        typeMapper.setTrustedPackages("*");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
