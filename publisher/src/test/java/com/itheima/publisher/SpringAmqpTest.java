package com.itheima.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue() {
        // 1.队列名
        String queueName = "simple.queue";
        // 2.消息
        String message = "Hello, spring amqp!";
        // 3.发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testWorkQueue() {
        // 1.队列名
        String queueName = "work.queue";
        // 2.消息
        for (int i = 0; i < 50; i++) {
            String message = "Hello, spring amqp!" + i;
            // 3.发送消息
            rabbitTemplate.convertAndSend(queueName, message);
        }
    }

    @Test
    public void testFanoutQueue() {
        // 1.交换机名称
        String exchangeName = "hmall.fanout";
        // 2.消息
        String message = "Hello, everyone!";
        // 3.发送消息
        rabbitTemplate.convertAndSend(exchangeName, null, message);
    }

    @Test
    public void testDirectQueue() {
        // 1.交换机名称
        String exchangeName = "hmall.direct";
        // 2.消息
        String message = "蓝色：震惊，大学男宿舍！";
        // 3.发送消息
        rabbitTemplate.convertAndSend(exchangeName, "yellow", message);
    }

    @Test
    public void testTopicQueue() {
        // 1.交换机名称
        String exchangeName = "hmall.topic";
        // 2.消息
        String message = "天气111！";
        // 3.发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.weather", message);
    }

    @Test
    public void testSendObject() {
        // 1.准备消息
        rabbitTemplate.convertAndSend("object.queue", new User("李四", 18, "男"));
    }

    @Test
    public void testConfirmCallback() throws InterruptedException {
        // 0. 创建correlationData
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("spring amqp 处理确认结果异常", ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 判断是否成功
                if (result.isAck()) {
                    log.debug("收到ConfirmCallback ack， 消息发送成功！");
                } else {
                    log.error("收到ConfirmCallback nack, 消息发送失败！reason：{}", result.getReason());
                }
            }
        });


        // 1.交换机名称
        String exchangeName = "hmall.topic";
        // 2.消息
        String message = "天气111！";
        // 3.发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.weather22", message, cd);

        Thread.sleep(2000);
    }

    @Test
    void testSendMessage() {
        // 1.自定义构建消息
        Message message = MessageBuilder
                .withBody("hello, springAMQP".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                .build();
        // 2.发送消息
        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend("lazy.queue", message);
        }
    }

    @Test
    void testSendDelayMessage() {
        rabbitTemplate.convertAndSend("normal.direct", "hi", "hello", message -> {
            message.getMessageProperties().setExpiration("10000");
            return message;
        });
    }

    @Test
    void testSendDelayByPlugin() {
        rabbitTemplate.convertAndSend("delay.direct", "hi", "hello,delay", message -> {
            message.getMessageProperties().setDelay(10000);
            return message;
        });
    }
}