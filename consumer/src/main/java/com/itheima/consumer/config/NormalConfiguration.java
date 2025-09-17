package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NormalConfiguration {

    @Bean
    public DirectExchange NormalExchange() {
        // return ExchangeBuilder.fanoutExchange("hmall.fanout").build();
        return new DirectExchange("normal.direct");
    }

    @Bean
    public Queue NormalQueue() {
        return QueueBuilder
                .durable("normal.queue")
                .deadLetterExchange("dlx.direct")
                .build();
    }

    @Bean
    public Binding normalExchangeBinding(Queue NormalQueue, DirectExchange NormalExchange) {
        return BindingBuilder.bind(NormalQueue).to(NormalExchange).with("hi");
    }
}
