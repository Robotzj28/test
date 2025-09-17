package com.itheima.publisher.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class mqConfig {

    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            log.error("监听到了消息的return callback");
            log.debug("交换机：{}", returnedMessage.getExchange());
            log.debug("路由键：{}", returnedMessage.getRoutingKey());
            log.debug("message：{}", returnedMessage.getMessage());
            log.debug("replyText：{}", returnedMessage.getReplyText());
            log.debug("replyCode：{}", returnedMessage.getReplyCode());
        });
    }
}
