package com.itheima.consumer.mq;

import com.itheima.publisher.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Component
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(Message message) {
        log.info("监听到simple.queue的消息：ID：【{}】", message.getMessageProperties().getMessageId());
        log.info("监听到simple.queue的消息：【{}】", new String(message.getBody()));
//        throw new RuntimeException("我是故意的！");
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String message) throws InterruptedException {
        System.out.println("消费者1接收到消息：" + message + "," + LocalTime.now());
        Thread.sleep(25);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String message) throws InterruptedException {
        System.err.println("消费者2.....接收到消息：" + message + "," + LocalTime.now());
        Thread.sleep(200);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String message) throws InterruptedException {
        log.info("fanout.queue1 接收到消息：【{}】", message);
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String message) throws InterruptedException {
        log.info("fanout.queue2 接收到消息：【{}】", message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1", durable = "true"),
            exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenDirectQueue1(String message) throws InterruptedException {
        log.info("Direct.queue1 接收到消息：【{}】", message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2", durable = "true"),
            exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenDirectQueue2(String message) throws InterruptedException {
        log.info("Direct.queue2 接收到消息：【{}】", message);
    }

    @RabbitListener(queues = "topic.queue1")
    public void listenTopicQueue1(String message) throws InterruptedException {
        log.info("topic.queue1 接收到消息：【{}】", message);
    }

    @RabbitListener(queues = "topic.queue2")
    public void listenTopicQueue2(String message) throws InterruptedException {
        log.info("topic.queue2 接收到消息：【{}】", message);
    }

    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(User message){
        log.info("object.queue2 接收到消息：【{}】", message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "dlx.queue", durable = "true"),
            exchange = @Exchange(name = "dlx.direct", type = ExchangeTypes.DIRECT),
            key = {"hi"}
    ))
    public void listenDlxQueue(String message) throws InterruptedException {
        log.info("dlx.queue 接收到消息：【{}】", message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "delay.queue", durable = "true"),
            exchange = @Exchange(name = "delay.direct", delayed = "true"),
            key = {"hi"}
    ))
    public void listenDelayQueue(String message) throws InterruptedException {
        log.info("delay.queue 接收到消息：【{}】", message);
    }
}
