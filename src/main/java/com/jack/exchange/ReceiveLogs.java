package com.jack.exchange;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs {
    private static final String EXCAHNGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCAHNGE_NAME, BuiltinExchangeType.FANOUT);
        final String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCAHNGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

       DeliverCallback deliverCallback = (consumerTag, delivery) -> {
           final String message = new String(delivery.getBody(), "UTF-8");
           System.out.println(" [x] Received'" + message + "'");

       };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
        });

    }
}
