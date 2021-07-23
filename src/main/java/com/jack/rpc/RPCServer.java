package com.jack.rpc;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n) {
        if(n == 0) return  0;
        if (n ==1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) throws Exception{
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel()){
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false,null);
            channel.queuePurge(RPC_QUEUE_NAME);
            channel.basicQos(1);
            System.out.println(" [x] Awaiting RPC requests");

            final Object monitor = new Object();
            DeliverCallback deliverCallBack = (consumerTag, delivery) -> {
                final AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();
                String response = "";
                try {
                    final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    final int n = Integer.parseInt(message);
                    System.out.println("[.] fib(" + message + ")");
                    response += fib(n);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } finally {
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes(StandardCharsets.UTF_8));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallBack, (consumerTag -> {
            }));
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
