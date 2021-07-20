package com.jack.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsTopic {
    private static final String EXCAHNGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCAHNGE_NAME, BuiltinExchangeType.TOPIC);
        final String queue = channel.queueDeclare().getQueue();
        if (args.length < 1) {
            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
            System.exit(1);
        }
        for (String bindingKey : args) {
            channel.queueBind(queue, EXCAHNGE_NAME, bindingKey);
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

       DeliverCallback deliverCallback = (consumerTag, delivery) -> {
           final String message = new String(delivery.getBody(), "UTF-8");
           System.out.println(" [x] Received'" + delivery.getEnvelope().getRoutingKey() + ":" + message + "'");

       };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
        });

    }
}
