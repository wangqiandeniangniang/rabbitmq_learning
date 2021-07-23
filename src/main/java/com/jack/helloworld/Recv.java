package com.jack.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author liangchen
 * @date 2021/6/3
 */
public class Recv {
    private final static String QUEUE_NAME = "test_queue";
    private final static String V_HOST = "test_host";


    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost(V_HOST);
        factory.setHandshakeTimeout(1000000);
        factory.setConnectionTimeout(1000000);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        // 需要使用jdk1.8,因为使用lambada表达式
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String (delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

    }
}
