package com.jack.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author liangchen
 * @date 2021/6/3
 */
public class Send {

    private final static String QUEUE_NAME = "test_queue";
    private final static String V_HOST = "test_host";


    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost(V_HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //因为在第一步已经设置好队列了
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World";
        // 向队列test_queue发送消息
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println("[x] Sent '" + message + "'");
        //打印消息数量
        System.out.println(channel.messageCount(QUEUE_NAME));

    }
}
