package cn.itcast.mq.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //Basic Queue 简单队列模型 消息发送
    @Test
    public void testSendMessage2SimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, spring amqp!";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }


    /**
     * workQueue
     * 向队列中不停发送消息，模拟消息堆积。
     * 循环发送，模拟大量消息堆积现象.
     */
    @Test
    public void testSendMessage2WorkQueue() throws InterruptedException {
        String queueName = "simple.queue2";
        String message = "hello, message__";
        for (int i = 1; i <= 50; i++) {
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }

    @Test
    public void testSendFanoutExchange() {
        // 交换机名称
        String exchangeName = "itcast.fanout";
        // 消息
        String message = "hello, every one!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }


    @Test
    public void testSendDirectExchange() {
        // 交换机名称
        String exchangeName = "itcast.direct";
        // 消息
        String message1 = "hello, yellow!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "yellow", message1);

        String message2 = "hello, blue!";
        rabbitTemplate.convertAndSend(exchangeName, "blue", message2);

        String message3 = "hello, red!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "red", message3);
    }

    // Topic 交换机
    @Test
    public void testSendTopicExchange() {
        // 交换机名称
        String exchangeName = "itcast.topic";
        // 消息
        String message = "今天天气不错，我的心情好极了!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.weather", message);
        // 消息
        String message2 = "中国新闻!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message2);
        // 消息
        String message3 = "美国新闻!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "US.news", message3);
    }

    @Test
    public void testSendMap() throws InterruptedException {
// 准备消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("name", "Jack");
        msg.put("age", 21);
// 发送消息
        rabbitTemplate.convertAndSend("simple.queue", "", msg);
    }

}
